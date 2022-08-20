package android.core

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import org.koin.core.module.Module
import setAppLocale
import timber.log.Timber

open class CoreApplication: Application() {

    protected open fun module(): Module? = null
    protected open fun locale() = "id"
    protected open fun onLaunch() {
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapper(newBase?.setAppLocale(locale())))
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        onLaunch()
        startKoin {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(applicationContext)
            module()?.let { modules(it) }
        }
    }
}