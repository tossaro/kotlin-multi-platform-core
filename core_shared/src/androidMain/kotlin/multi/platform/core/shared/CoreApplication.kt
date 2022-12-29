package multi.platform.core.shared

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex
import io.ktor.http.*
import multi.platform.core.shared.external.utility.LocaleUtil
import multi.platform.core.shared.external.utility.SharedPrefsUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.EmptyLogger
import org.koin.core.module.Module
import org.koin.dsl.module
import timber.log.Timber

abstract class CoreApplication : Application() {

    /**
     * Abstract function must override shared preferences name
     */
    abstract fun sharedPrefsName(): String

    /**
     * Abstract function must override server protocol
     */
    abstract fun protocol(): URLProtocol

    /**
     * Abstract function must override server host
     */
    abstract fun host(): String

    /**
     * Abstract function must override app version
     */
    abstract fun appVersion(): String

    /**
     * Abstract function must override device id
     */
    abstract fun deviceId(): String

    /**
     * Abstract function must override KOIN module
     */
    abstract fun koinModule(): Module

    /**
     * Open function for add initialize application
     * Triggered by onCreate
     * Super: Timber
     */
    @CallSuper
    protected open fun onLaunch() {
        Timber.plant(Timber.DebugTree())
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtil.onAttach(newBase))
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtil.onAttach(this)
    }

    override fun onCreate() {
        super.onCreate()
        onLaunch()
        initKoin(this, host(), protocol(), sharedPrefsName(), deviceId(), appVersion()) {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(applicationContext)
            modules(
                module {
                    single { SharedPrefsUtil.build(get(), sharedPrefsName()) }
                },
                koinModule()
            )
        }
    }
}