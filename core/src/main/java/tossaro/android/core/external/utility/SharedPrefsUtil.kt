package tossaro.android.core.external.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import tossaro.android.core.R


object SharedPrefsUtil {
    fun build(context: Context): SharedPreferences {
        val masterKey: MasterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "pri0r_" + context.getString(R.string.app_name).replace(" ", "_").replace("(", "")
                .replace(")", "") + "_h1ght",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}