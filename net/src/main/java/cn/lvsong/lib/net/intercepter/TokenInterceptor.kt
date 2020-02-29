package cn.lvsong.lib.net.intercepter

import android.util.Log
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * http://www.mamicode.com/info-detail-1375498.html
 * Token
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
                "X-Access-Token",
                MMKV.defaultMMKV().decodeString(cn.lvsong.lib.library.utils.Constants.KEY_REQUEST_TOKEN, "")
            ) // TODO
            .build()
        //
        Log.i(
            "TokenInterceptor",
            "=======X-Access-Token : " + MMKV.defaultMMKV().decodeString(cn.lvsong.lib.library.utils.Constants.KEY_REQUEST_TOKEN, "")
        )
        return chain.proceed(newRequest)
    }

}
