package multi.platform.core.shared.external.utility

import multi.platform.core.shared.Context
import multi.platform.core.shared.domain.common.usecase.RefreshTokenUseCase
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.constant.HttpHeaderConstant
import multi.platform.core.shared.external.extension.toMD5
import multi.platform.core.shared.getLanguage
import multi.platform.core.shared.getPlatform
import multi.platform.core.shared.getStringPref
import multi.platform.core.shared.putStringPref
import multi.platform.core.shared.removePref
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
class ApiClient(
    private val context: Context,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val sharedPrefsKey: String,
    private val server: String,
    private val deviceId: String,
    private val version: String,
    private val channel: String = AppConstant.CHANNEL,
) {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = server
            }
            header(HttpHeaderConstant.DEVICE_ID, deviceId.toMD5())
            header(HttpHeaderConstant.LANGUAGE, getLanguage(context))
            header(HttpHeaderConstant.OS, getPlatform().name)
            header(HttpHeaderConstant.VERSION, version)
            header(HttpHeaderConstant.CHANNEL, channel)
        }
        expectSuccess = true
        install(HttpRequestRetry) {
            retryIf { _, response ->
                response.status.value == 401
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