package cn.lvsong.lib.net.download

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @ProjectName: android
 * @ClassName: JsDownloadInterceptor
 * @Description: Response拦截器
 * @Author: Jooyer
 */
class JDownloadInterceptor(private val downloadListener: JDownloadListener) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        return response.newBuilder().body(
            response.body?.let { JResponseBody(it, downloadListener) }
        ).build()
    }
}