package cn.lvsong.lib.demo.util

import cn.lvsong.lib.demo.api.WanApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Desc:
 * Author: Jooyer
 * Date: 2020-08-29
 * Time: 22:21
 */
object NetUtil {

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build())
        .baseUrl("https://www.wanandroid.com/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory()) //  https://blog.csdn.net/weixin_31080131/article/details/112591472 ,参考第三种写法
        .addConverterFactory(MoshiConverterFactory.create())
        .build()


    val apiService: WanApi = retrofit.create(WanApi::class.java)

}