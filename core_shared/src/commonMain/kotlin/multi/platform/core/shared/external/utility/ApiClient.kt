package multi.platform.core.shared.external.utility

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import multi.platform.core.shared.*
import multi.platform.core.shared.domain.common.usecase.RefreshTokenUseCase
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.constant.HttpHeaderConstant

class ApiClient(
    private val context: Context?,
    httpClientEngine: HttpClientEngine,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val json: Json,
    private val server: String,
    private val serverProtocol: URLProtocol,
    private val sharedPrefsKey: String,
    private val deviceId: String,
    private val version: String,
    private val channel: String = AppConstant.CHANNEL,
) {
    val client = HttpClient(httpClientEngine) {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url {
                protocol = serverProtocol
                host = server
            }
            header(HttpHeaderConstant.DEVICE_ID, deviceId)
            header(HttpHeaderConstant.LANGUAGE, getLanguage(context))
            header(HttpHeaderConstant.OS, getPlatform().name)
            header(HttpHeaderConstant.VERSION, version)
            header(HttpHeaderConstant.CHANNEL, channel)
        }
        expectSuccess = true
        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value
                println("HTTP status: $statusCode")
                when (statusCode) {
                    in 300..399 -> throw RedirectResponseException(response, "Unhandled redirect")
                    in 400..499 -> throw ClientRequestException(response, "Client request invalid")
                    in 500..599 -> throw ServerResponseException(response, "Server error")
                }
                if (statusCode >= 600) {
                    throw ResponseException(response, "Bad response")
                }
            }
        }
        install(HttpRequestRetry) {
            retryIf { httpRequest, httpResponse ->
                httpResponse.status.value == 401
                        && !httpRequest.url.fullPath.contains("/v1/token", ignoreCase = true)
                        && !httpRequest.url.fullPath.contains(
                    "/v1/authorization",
                    ignoreCase = true
                )
            }
            modifyRequest { request ->
                runBlocking {
                    try {
                        var refreshToken =
                            getStringPref(context, sharedPrefsKey, AppConstant.REFRESH_TOKEN, null)
                        val phone = getStringPref(context, sharedPrefsKey, AppConstant.PHONE, null)
                        if (refreshToken != null && phone != null) {
                            val response = refreshTokenUseCase(refreshToken, phone)
                            val token = response?.data?.session?.token
                            refreshToken = response?.data?.session?.refreshToken
                            putStringPref(context, sharedPrefsKey, AppConstant.ACCESS_TOKEN, token)
                            putStringPref(
                                context,
                                sharedPrefsKey,
                                AppConstant.REFRESH_TOKEN,
                                refreshToken
                            )
                            request.headers { remove(HttpHeaders.Authorization) }
                            request.bearerAuth(token.toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        removePref(context, sharedPrefsKey, AppConstant.ACCESS_TOKEN)
                        removePref(context, sharedPrefsKey, AppConstant.REFRESH_TOKEN)
                        removePref(context, sharedPrefsKey, AppConstant.PHONE)
                    }
                }
            }
        }
    }
}