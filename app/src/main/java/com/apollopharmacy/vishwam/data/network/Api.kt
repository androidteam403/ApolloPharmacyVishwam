package com.apollopharmacy.vishwam.data.network

import com.apollopharmacy.vishwam.BuildConfig
import com.apollopharmacy.vishwam.data.Config.URL_MAIN
import com.apollopharmacy.vishwam.data.ViswamAppApi
import com.apollopharmacy.vishwam.util.Utils
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

class Api {
    companion object {
        var retrofit: Retrofit? = null
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            if (Utils.IS_LOG_ENABLED) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        fun getClient(): ViswamAppApi {
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(OkHttpProfilerInterceptor())
            }

            val okHttpClient = builder
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .hostnameVerifier(HostnameVerifier { hostname, session -> true })
                .build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(URL_MAIN)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ViswamAppApi::class.java)
        }
    }
}