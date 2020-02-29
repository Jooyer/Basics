package cn.lvsong.lib.net.retrofit

import android.content.Context
import android.text.TextUtils
import android.util.Log
import cn.lvsong.lib.net.cover.DataConverterFactory
import cn.lvsong.lib.net.cover.Expose2ConverterFactory
import cn.lvsong.lib.net.cover.Expose3ConverterFactory
import cn.lvsong.lib.net.cover.ExposeConverterFactory
import cn.lvsong.lib.net.intercepter.LoggingInterceptor
import cn.lvsong.lib.net.intercepter.TokenInterceptor
import cn.lvsong.lib.net.utils.RetryWithDelay
import io.reactivex.Maybe
import io.reactivex.MaybeTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * 初始化
 * --> 获取全局上下文对象
 *
 *
 * 可以通过 setLevel 改变日志级别
 * 共包含四个级别：NONE、BASIC、HEADER、BODY
 *
 *
 * NONE 不记录
 *
 *
 * BASIC 请求/响应行
 * --> POST /greeting HTTP/1.1 (3-byte body)
 * <-- HTTP/1.1 200 OK (22ms, 6-byte body)
 *
 *
 * HEADER 请求/响应行 + 头
 *
 *
 * --> Host: example.com
 * Content-Type: plain/text
 * Content-Length: 3
 *
 *
 * <-- HTTP/1.1 200 OK (22ms)
 * Content-Type: plain/text
 * Content-Length: 6
 *
 *
 * BODY 请求/响应行 + 头 + 体
 *
 *
 *
 *
 *
 *
 * Created by Jooyer on 2017/2/14
 */
class RxRetrofit private constructor() {
    private lateinit var mRetrofit: Retrofit
    lateinit var mContext: Context


    //    @SuppressWarnings("unchecked")
    //    public void request(RxAppCompatActivity activity, Observable<String> observable, Observer<String> observer) {
    //        observable
    //                .retryWhen(new RetryWhenNetWorkException())
    //                .compose(activity.bindUtilEvent(ActivityLifeCycleEvent.STOP)) // 生命周期管理
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribeOn(Schedulers.io())
    //                .onErrorResumeNext(funcException)
    //                .subscribe(observer);
    //    }

    //    @SuppressWarnings("unchecked")
    //    public void request(RxFragment fragment, Observable<String> observable, Observer<String> observer) {
    //        observable
    //                .retryWhen(new RetryWhenNetWorkException())
    //                .compose(fragment.bindUtilEvent(FragmentLifeCycleEvent.STOP)) // 生命周期管理
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribeOn(Schedulers.io())
    //                .onErrorResumeNext(funcException)
    //                .subscribe(observer);
    //    }


    //    @SuppressWarnings("unchecked")
    //    public void request(Observable<String> observable, Observer<String> observer) {
    //        observable
    //                .retryWhen(new RetryWhenNetWorkException())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribeOn(Schedulers.io())
    //                .onErrorResumeNext(funcException)
    //                .subscribe(observer);
    //    }

    //    @SuppressWarnings("unchecked")
    //    public void requestResponseBody(Observable<ResponseBody> observable, Observer<ResponseBody> observer) {
    //        observable
    //                .retryWhen(new RetryWhenNetWorkException())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribeOn(Schedulers.io())
    //                .onErrorResumeNext(funcException)
    //                .subscribe(observer);
    //    }

    /**
     * 异常处理类
     */

    //    private Function funcException = new Function<Throwable, ObservableSource>() {
    //        @Override
    //        public ObservableSource apply(@NonNull Throwable throwable) throws Exception {
    //            return Observable.error(ExceptionFactory.analysisException(throwable));
    //        }
    //    };


    /**
     * @param context                 --> 用来初始化缓存目录
     * @param baseUrl                 --> Retrofit 需要的基础 URL
     * @param isAddLoggingInterceptor --> 是否添加请求日志
     * @param cacheName               --> 缓存目录,默认是 " OkHttpCache "
     */
    fun init(context: Context, cacheName: String, baseUrl: String, isAddLoggingInterceptor: Boolean = false) {
        mContext = context
        initClient(baseUrl, isAddLoggingInterceptor, cacheName)
    }

    /**
     * 初始化 OKHttp
     *
     * @param cacheName               --> 缓存目录,默认是 " OkHttpCache "
     * @param isAddLoggingInterceptor 是否添加日志输入   true --> 添加
     */
    private fun initClient(baseUrl: String, isAddLoggingInterceptor: Boolean, cacheName: String) {
        val builder = OkHttpClient.Builder()
        //超时设置
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        //设置缓存
        val okHttpCache =
            File(mContext.externalCacheDir, if (TextUtils.isEmpty(cacheName)) "OkHttpCache" else cacheName)
        builder.cache(Cache(okHttpCache, (50 * 1024 * 1024).toLong()))

        // 推荐使用下面这种缓存方式
//        builder.addInterceptor(OffLineCacheInterceptor())
//        builder.addNetworkInterceptor(NetCacheInterceptor())

        // token
        builder.addInterceptor(TokenInterceptor())

        // 日志拦截
//        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i(TAG, "Retrofit=========== $message") })
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        builder.addInterceptor(interceptor)
        builder.retryOnConnectionFailure(true)
        // 打印日志,根据需要设置
        if (isAddLoggingInterceptor) {
            builder.addInterceptor(LoggingInterceptor())
        }
        Log.e("RxRetrofit","baseUrl==========$baseUrl")
        mRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            // 转换器按添加顺序匹配
//            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ExposeConverterFactory.create())
            .addConverterFactory(Expose2ConverterFactory.create())
            .addConverterFactory(Expose3ConverterFactory.create())
            .addConverterFactory(DataConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }

    fun <T> getApi(service: Class<T>): T {
        return mRetrofit.create(service)
    }

    // https://www.jianshu.com/p/4005bc4a20f2?from=jiantop.com
    fun <T> schedulersTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        }
    }

    // https://www.jianshu.com/p/45309538ad94
    fun <T> maybeTransformer(): MaybeTransformer<T, T> {
        return MaybeTransformer {
            it.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhenDelayMillis()
        }
    }

    /** 网络异常重试
     * @param maxRetries --> 重试次数,默认3次
     * @param retryDelayMillis --> 每次请求延迟时间,默认500 ms
     */
    // https://www.jianshu.com/p/0db849740285
    private fun <T> Maybe<T>.retryWhenDelayMillis(maxRetries: Int = 3, retryDelayMillis: Long = 500): Maybe<T> {
        return this.retryWhen(RetryWithDelay(maxRetries, retryDelayMillis))
    }

    companion object {
        private val TAG = RxRetrofit::class.java.simpleName
        private val DEFAULT_TIMEOUT = 20L
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RxRetrofit()
        }
    }
}


/* --------------------------- 拓展函数 --------------------------------- */
/** 请求出错处理,同时捕获异常
 *@param defValue --> 出错返回默认的提醒
 */
fun <T> Maybe<T>.errorReturn(defValue: T): Maybe<T> =
    this.onErrorReturn { it ->
        it.printStackTrace()
        return@onErrorReturn defValue
    }

/** 请求出错处理,同时捕获异常
 *@param defValue --> 出错返回默认的提醒
 *@param action --> 对错误的处理
 */
fun <T> Maybe<T>.errorReturn(defValue: T, action: (Throwable) -> Unit): Maybe<T> =
    this.onErrorReturn {
        action.invoke(it)
        return@onErrorReturn defValue
    }

/** 遇到错误时,能够提前捕获异常,并返回一个新的 Maybe,后面无需再做异常处理
 */
fun <T> Maybe<T>.errorResumeNext(): Maybe<T> =
    this.onErrorResumeNext(Maybe.empty())

/** 遇到错误时,能够提前捕获异常,并返回一个新的 Maybe,后面无需再做异常处理
 * @param defValue --> 出错返回默认的提醒
 */
fun <T> Maybe<T>.errorResumeNext(defValue: T): Maybe<T> =
    this.onErrorResumeNext(Maybe.just(defValue))

