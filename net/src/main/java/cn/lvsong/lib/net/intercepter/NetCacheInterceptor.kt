package cn.lvsong.lib.net.intercepter

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 有网时缓存
 * Created by Jooyer on 2018/4/9
 * https://www.jianshu.com/p/dbda0bb8d541
 */

class NetCacheInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())


        val cacheControl = response.header("Cache-Control")
        return if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                "no-cache"
            ) ||
            cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
        ) {
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$ONLINE_CACHE_TIME")
                .build()
        } else {
            response
        }

        //        return response.newBuilder()
        //                .header("Cache-Control", "public," +
        //                        // 没有超出max-age,不管怎么样都是返回数据，超过了maxAge,发起新的请求获取数据更新，请求失败返回缓存数据
        //                        " max-age=" + ONLINE_CACHE_TIME)
        //                .removeHeader("Pragma")
        //                .build();
    }

    companion object {
        // 在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为 0
        private val ONLINE_CACHE_TIME = 30 // 单位 秒
    }
}
