package cn.lvsong.lib.net.download;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @ProjectName: android
 * @ClassName: JsDownloadInterceptor
 * @Description: Response拦截器
 * @Author: Jooyer
 * @CreateDate: 2020/6/16 18:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/16 18:18
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class JsDownloadInterceptor implements Interceptor {
    private JsDownloadListener downloadListener;
    public JsDownloadInterceptor(JsDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new JsResponseBody(response.body(), downloadListener)).build();
    }
}
