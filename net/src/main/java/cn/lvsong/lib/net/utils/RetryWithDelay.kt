package cn.lvsong.lib.net.utils

import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Desc: 网络请求异常,重试机制
 * Author: Jooyer
 * Date: 2018-08-03
 * Time: 14:58
 */
class RetryWithDelay(private val maxRetries: Int, private val retryDelayMillis: Long) :
    Function<Flowable<out Throwable>, Publisher<*>> {
    private var retryCount: Int = 0
    override fun apply(t: Flowable<out Throwable>): Publisher<*> {
        // 所有的网络异常都属于I/O异常
        return t.flatMap(Function<Throwable, Publisher<*>> { throwable ->
            if (throwable is IOException) { // IO 异常时才重试
                if (++retryCount <= maxRetries) {
                    println("RetryWithDelay============$retryCount ---> $maxRetries")
                    Flowable.timer(retryDelayMillis, TimeUnit.MILLISECONDS)
                } else {
                    println("RetryWithDelay============${throwable.message}")
                    Flowable.error<Any>(throwable)
                }
            } else {
                Flowable.error<Any>(throwable)
            }

        })
    }
}
