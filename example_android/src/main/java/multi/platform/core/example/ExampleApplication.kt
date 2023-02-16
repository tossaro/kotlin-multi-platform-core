package multi.platform.core.example

import android.provider.Settings
import multi.platform.core.shared.CoreApplication
import multi.platform.core.shared.external.extension.toMD5
import multi.platform.example_lib.shared.libModule
import multi.platform.example_lib.shared.prefsNameShared
import multi.platform.example_lib.shared.protocolShared
import io.ktor.http.*

class ExampleApplication : CoreApplication() {
    override fun host() = BuildConfig.SERVER
    override fun protocol() = protocolShared()
    override fun sharedPrefsName() = prefsNameShared()
    override fun deviceId() = getString(R.string.app_version)
    override fun koinModule() = libModule()

    @Suppress("HardwareIds")
    override fun appVersion() =
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            .toString().toMD5()
}