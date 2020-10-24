package cn.lvsong.lib.net.intercepter

import cn.lvsong.lib.net.utils.NetWorkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Jooyer on 2018/4/9
 * 没有网络时缓存
 */
/**
 * @param offlineCacheTime --> 离线缓存过期时,默认60秒
 */
class OffLineCacheInterceptor(val offlineCacheTime: Int = 60) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetWorkUtil.isNetWorkAvailable()) {
            request = request.newBuilder()
                .removeHeader("Pragma")
                // https://stackoverflow.com/questions/42927216/retrofitokhttp-http-504-unsatisfiable-request-only-if-cached
                //            .cacheControl(new CacheControl
                //                                .Builder()
                //                                .maxStale(60,TimeUnit.SECONDS)
                //                                .onlyIfCached()
                //                                .build()
                //                        ) 两种方式结果是一样的，写法不同


                // 使用 Cache-Control : only-if-cached header，报文将永远也不会到达服务器。只有存在可用缓存响应时才会检查并返回它。
                // 不然的话，会抛出 504 错误，所以开发的时候别忘了处理这个异常。

                // 使用 Cache-Control : max-stale=[seconds] 报头，这种办法更灵活一些，它向客户端表明可以接收缓存响应，
                // 只要该缓存响应没有超出其生命周期。而该缓存响应是否可用由 OkHttp 检查，所以不需要与服务器建立连接来判断。
                // 但即使如此，当缓存响应超出其生命周期，网络操作还是会进行，然后得到服务器返回的新内容
                .header(
                    "Cache-Control", "public, only-if-cached, " +
                            // 没有超过maxStale，不管怎么样都返回缓存数据，超过了maxStale,发起请求获取更新数据，请求失败返回失败
                            "max-stale=" + offlineCacheTime
                )
                .build()
        }

        return chain.proceed(request)
    }

}
