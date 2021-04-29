package cn.lvsong.lib.net.download

/**
 * @ProjectName: android
 * @ClassName: JsDownloadListener
 * @Description: 下载的回调
 * @Author: Jooyer
 * @CreateDate: 2020/6/16 18:15
 */
interface JDownloadListener {
    fun onStartDownload(totalLength: Long)
    fun onProgress(progress: Int)
    fun onComplete()
    fun onFail(errorInfo: String?)
}