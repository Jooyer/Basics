package cn.lvsong.lib.net.download

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *
 * @ProjectName:    android
 * @ClassName:      DownloadService
 * @Description:  使用 DownWorker 下载时参考用的 API 接口
 * @Author:         Jooyer
 * @CreateDate:     2020/6/16 16:40
 */
interface DownloadService {

    @Streaming
    @GET
    fun downloadApp(@Url url: String): Call<ResponseBody>

}