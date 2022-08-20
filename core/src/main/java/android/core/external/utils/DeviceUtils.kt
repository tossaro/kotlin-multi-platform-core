package android.core.external.utils

import android.content.Context
import android.provider.Settings

class DeviceUtils(val context: Context) {
    fun getDeviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}