package cn.lvsong.lib.net.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * @ProjectName: android
 * @ClassName: JsResponseBody
 * @Description: 对下载文件进行进度监听
 * @Author: Jooyer
 * @CreateDate: 2020/6/16 18:14
 * @Version: 1.0
 */
class JResponseBody(private val responseBody: ResponseBody, val downloadListener: JDownloadListener) :
    ResponseBody() {

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private var bufferedSource: BufferedSource? = null

    /**
     * 下载进度
     */
    private var mLatestProgress = 0

    init {
        downloadListener.onStartDownload(responseBody.contentLength())
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
//        Log.e("JsResponseBody", "==========source");
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                //                Log.e("JsResponseBody", "==========read: " + (int) (totalBytesRead * 100F / responseBody.contentLength()));
                if (null != downloadListener) {
                    if (bytesRead != -1L) {
                        val progress = (totalBytesRead * 100 / responseBody.contentLength()).toInt()
                        if (mLatestProgress != progress) {
                            downloadListener.onProgress(progress)
                            mLatestProgress = progress
                        }
                        if (totalBytesRead >= responseBody.contentLength()) { // 读取完成
                            downloadListener.onComplete()
                        }
                    }
                }
                return bytesRead
            }
        }
    }

}