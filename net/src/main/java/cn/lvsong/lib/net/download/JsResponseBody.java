package cn.lvsong.lib.net.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @ProjectName: android
 * @ClassName: JsResponseBody
 * @Description: 对下载文件进行进度监听
 * @Author: Jooyer
 * @CreateDate: 2020/6/16 18:14
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 */
public class JsResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private JsDownloadListener downloadListener;
    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;
    private int mLatestProgress = 0;

    public JsResponseBody(ResponseBody responseBody, JsDownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
        downloadListener.onStartDownload(responseBody.contentLength());
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
//        Log.e("JsResponseBody", "==========source");
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
//                Log.e("JsResponseBody", "==========read: " + (int) (totalBytesRead * 100F / responseBody.contentLength()));
                if (null != downloadListener) {
                    if (bytesRead != -1) {
                        int progress = (int) (totalBytesRead * 100 / responseBody.contentLength());
                        if (mLatestProgress != progress) {
                            downloadListener.onProgress(progress);
                            mLatestProgress = progress;
                        }
                        if (totalBytesRead >= responseBody.contentLength()){ // 读取完成
                            downloadListener.onComplete();
                        }
                    }
                }
                return bytesRead;
            }
        };
    }
}