package tossaro.android.core.external.utility

import android.content.Context
import android.content.pm.PackageManager
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tossaro.android.core.BuildConfig
import tossaro.android.core.external.constant.TimeoutConstant.OKHTTP_CONNECTION_TIMEOUT
import tossaro.android.core.external.constant.TimeoutConstant.OKHTTP_READ_TIMEOUT
import tossaro.android.core.external.constant.TimeoutConstant.OKHTTP_WRITE_TIMEOUT
import java.util.concurrent.TimeUnit


object NetworkUtil {
    fun buildClient(applicationContext: Context, requestInterceptor: Interceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val builder = OkHttpClient.Builder()
        try {
            builder.connectTimeout(OKHTTP_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            builder.readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            builder.writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
            builder.addInterceptor(httpLoggingInterceptor)

            val chuckerCollector = ChuckerCollector(
                context = applicationContext,
                showNotification = true,
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )

            val chuckerInterceptor = ChuckerInterceptor.Builder(applicationContext)
                .collector(chuckerCollector)
                .maxContentLength(250_000L)
                .alwaysReadResponseBody(true)
                .build()
            builder.addInterceptor(chuckerInterceptor)
            if (BuildConfig.DEBUG) builder.addNetworkInterceptor(StethoInterceptor())
            builder.addNetworkInterceptor(requestInterceptor)
            builder.connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS))
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return builder.build()
    }

    inline fun <reified T> buildService(baseUrl: String, okHttpClient: OkHttpClient): T {
        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()

        return retrofit.create(T::class.java)
    }
}