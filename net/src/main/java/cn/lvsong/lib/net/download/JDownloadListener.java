package cn.lvsong.lib.net.download;

/**
 * @ProjectName: android
 * @ClassName: JsDownloadListener
 * @Description:
 * @Author: Jooyer
 * @CreateDate: 2020/6/16 18:15
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/16 18:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface JDownloadListener {
    void onStartDownload(long totalLength);
    void onProgress(int progress);
    void onComplete();
    void onFail(String errorInfo);
}
