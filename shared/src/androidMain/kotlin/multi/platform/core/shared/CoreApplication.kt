package multi.platform.core.shared

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex
import multi.platform.core.shared.data.common.CommonRepositoryImpl
import multi.platform.core.shared.domain.common.CommonRepository
import multi.platform.core.shared.domain.common.usecase.RefreshTokenUseCase
import multi.platform.core.shared.external.utility.LocaleUtil
import multi.platform.core.shared.external.utility.SharedPrefsUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import timber.log.Timber

abstract class CoreApplication : Application() {

    /**
     * Open function for override shared preferences name
     */
    protected open fun sharedPrefsName() = "pri0r_m1cr0_h1ght"

    /**
     * Open function for override KOIN module
     */
    protected open fun koinModule(): Module = module {}

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
        startKoin {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(applicationContext)
            modules(
                module {
                    single { SharedPrefsUtil.build(get(), sharedPrefsName()) }
                    single<CommonRepository> { CommonRepositoryImpl(get()) }
                    singleOf(::RefreshTokenUseCase)
                },
                koinModule()
            )
        }
    }
}