package cn.lvsong.lib.net.intercepter

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by Jooyer on 2016/12/19
 */
class LoggingInterceptor : Interceptor {
    private val UTF_8 = Charset.forName("UTF-8")
    private val TAG = "LoggerInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var param = ""
        val requestBody = request.body
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)

            var charset: Charset? = UTF_8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF_8)
            }
            param = buffer.readString(charset!!)
        }

        val t1 = System.nanoTime()
        Log.e(
            TAG, "=======request=======" + String.format(
                "Sending request %s on %s%n%s",
                request.url, chain.connection(), request.headers
            )
        )

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        Log.e(
            TAG, "=======response=======" + String.format(
                "Received response for %s in %.1fms%n%sconnection=%s",
                response.request.url, (t2 - t1) / 1e6, response.headers, chain.connection()
            ) + "\n request pram: " + param
        )
        return response
    }

}
