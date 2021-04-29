package cn.lvsong.lib.net.intercepter

import android.util.Log
import cn.lvsong.lib.net.utils.NetWorkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * http://www.mamicode.com/info-detail-1375498.html
 * Header 携带 Token, 下面是一个样例
 * Created by WZG on 2016/10/26.
 */

class TokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .addHeader("Accept", "application/json;charset=UTF-8")
            .addHeader(
                "Access-Token",NetWorkUtil.getAccessToken()
            )
            .build()
        return chain.proceed(newRequest)
    }

}
