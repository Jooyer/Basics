package cn.lvsong.lib.demo.util

import cn.lvsong.lib.demo.api.WanApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService: WanApi = retrofit.create(WanApi::class.java)

}