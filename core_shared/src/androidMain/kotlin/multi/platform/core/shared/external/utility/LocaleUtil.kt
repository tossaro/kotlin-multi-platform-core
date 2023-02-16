package multi.platform.core.shared.external.utility

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.preference.PreferenceManager
import java.util.*


object LocaleUtil {

    const val LOCALE_SELECTED = "LOCALE_SELECTED"
    const val ID = "ID"
    const val EN = "EN"
    const val AUTO = "AUTO"

    @Suppress("DEPRECATION", "kotlin:S1874")
    fun onAttach(c: Context?, l: String? = ID): Context? {
        var language = l
        if (l == AUTO) {
            language = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                c?.resources?.configuration?.locales?.get(0)?.language
            } else c?.resources?.configuration?.locale?.language
        }
        if (c != null && language != null) {
            val appLanguage = retrieveAppLanguage(c, language)
            return setLocale(c, appLanguage)
        }
        return null
    }

    fun retrieveAppLanguage(context: Context, defaultLanguage: String = ID): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(LOCALE_SELECTED, defaultLanguage)
            ?: defaultLanguage
    }

    fun setLocale(context: Context, language: String): Context {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(LOCALE_SELECTED, language)
        editor.apply()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }

    @Suppress("kotlin:S1874")
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)

        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)

        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION", "kotlin:S1874")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }
}
