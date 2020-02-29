package cn.lvsong.lib.demo.api

import cn.lvsong.lib.net.cover.TypeData
import io.reactivex.Maybe
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Desc:
 * Author: Jooyer
 * Date: 2020-01-10
 * Time: 11:30
 */
interface ApiService {

    @TypeData(dataKey = "data")
    @FormUrlEncoded
    @POST("image/test")
    fun test(@Field("key") key: String,@Field("data") data: String): Maybe<String>

}