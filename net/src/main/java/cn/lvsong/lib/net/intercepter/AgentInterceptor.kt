package cn.lvsong.lib.net.intercepter

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Jooyer on 2017/1/19
 */

class AgentInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("User-Agent", "Android")
            .build()
        return chain.proceed(newRequest)
    }
}
