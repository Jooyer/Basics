package cn.lvsong.lib.demo.api

import cn.lvsong.lib.demo.data.HomeData
import cn.lvsong.lib.demo.data.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Desc:
 * Author: Jooyer
 * Date: 2020-08-29
 * Time: 22:20
 */
interface WanApi {

    @GET("article/list/{page}/json")
    suspend fun getList(@Path("page") page: Int): Response<HomeData>


}