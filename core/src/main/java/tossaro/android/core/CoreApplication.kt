package tossaro.android.core

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import timber.log.Timber
import tossaro.android.core.external.constant.HttpHeaderConstant
import tossaro.android.core.external.utility.DeviceUtil
import tossaro.android.core.external.utility.NetworkUtil
import tossaro.android.core.external.utility.SharedPrefsUtil

abstract class CoreApplication : Application() {

    /**
     * Open function for override KOIN module
     */
    protected open fun koinModule(): Module = module {}

    /**
     * Open function for override request interceptor
     * Default: App-Version, Device-Id
     */
    protected open fun requestInterceptor(): Interceptor {
        val pInfo: PackageInfo =
            applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
        val version = pInfo.versionName
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header(HttpHeaderConstant.APP_VERSION, version)
                .header(HttpHeaderConstant.DEVICE_ID, DeviceUtil(applicationContext).getDeviceId())
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
    }

    /**
     * Open function for add initialize application
     * Triggered by onCreate
     * Super: Timber, Stetho
     */
    @CallSuper
    protected open fun onLaunch() {
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        onLaunch()
        startKoin {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(applicationContext)
            modules(
                module {
                    singleOf(::requestInterceptor)
                    single { SharedPrefsUtil.build(get()) }
                    single { NetworkUtil.buildClient(get(), get()) }
                },
                koinModule()
            )
        }
    }
}