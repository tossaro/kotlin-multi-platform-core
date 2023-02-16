package multi.platform.example_lib.shared

import io.ktor.http.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import multi.platform.core.shared.Context
import multi.platform.core.shared.initKoin
import multi.platform.example_lib.shared.domain.profile.usecase.*
import multi.platform.example_lib.shared.domain.stock.entity.Stock
import multi.platform.example_lib.shared.domain.stock.usecase.*

fun protocolShared() = URLProtocol.HTTPS
fun prefsNameShared() = "pri0r_m1cr0_h1ght_c0r3"
fun provideStockDb(): Realm {
    val config = RealmConfiguration.Builder(
        schema = setOf(Stock::class)
    ).build()
    return Realm.open(config)
}

@Suppress("UnUsed")
fun initKoin(
    context: Context?,
    host: String,
    deviceId: String,
    version: String,
) = initKoin(context, host, protocolShared(), prefsNameShared(), deviceId, version) {
    modules(libModule())
}