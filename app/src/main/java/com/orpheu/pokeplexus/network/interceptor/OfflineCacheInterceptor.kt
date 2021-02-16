package com.orpheu.pokeplexus.network.interceptor
import android.content.Context
import com.orpheu.pokeplexus.extension.hasActiveInternetConnection
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit

class OfflineCacheInterceptor(private val context: Context, private val maxStaleDays: Int) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (!context.hasActiveInternetConnection()) {
            val cacheControl = CacheControl.Builder()
                    .maxStale(maxStaleDays, TimeUnit.DAYS)
                    .build()

            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
        }

        return chain.proceed(request)
    }
}