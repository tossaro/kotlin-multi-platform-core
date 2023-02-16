package multi.platform.core.shared

import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import multi.platform.core.shared.data.common.CommonRepositoryImpl
import multi.platform.core.shared.domain.common.CommonRepository
import multi.platform.core.shared.domain.common.usecase.RefreshTokenUseCase
import multi.platform.core.shared.external.utility.ApiClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    context: Context?,
    server: String,
    serverProtocol: URLProtocol,
    sharedPrefsKey: String,
    deviceId: String,
    version: String,
    appDeclaration: KoinAppDeclaration = {}
): KoinApplication {
    return startKoin {
        modules(
            module {
                single { createJson() }
                single<CommonRepository> { CommonRepositoryImpl(get()) }
                singleOf(::RefreshTokenUseCase)
                single {
                    ApiClient(
                        context,
                        get(),
                        get(),
                        get(),
                        server,
                        serverProtocol,
                        sharedPrefsKey,
                        deviceId,
                        version
                    )
                }
            },
            platformModule(),
        )
        appDeclaration()
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun createJson() = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = false
}