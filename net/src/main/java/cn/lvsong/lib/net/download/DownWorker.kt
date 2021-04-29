package cn.lvsong.lib.net.download

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import cn.lvsong.lib.library.utils.FileUtil
import com.google.common.util.concurrent.ListenableFuture
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.InputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**https://blog.csdn.net/wuyuxing24/article/details/80955646
 * https://blog.csdn.net/yingaizhu/article/details/105392459
 * https://blog.csdn.net/weixin_42730358/article/details/105326794
 * https://www.jianshu.com/p/399f8b8cf736
 *
 * @ProjectName:    android
 * @ClassName:      DownWork
 * @Description:    下载APP的任务,工作中大家参考下面使用
 * @Author:         Jooyer
 * @CreateDate:     2020/6/16 15:49
 * @Version:        1.0
 */


/*  调用方法：

         val worker = OneTimeWorkRequestBuilder<DownWorker>()
                        .setInputData(Data.Builder()
                                .putString(DownWorker.DOWNLOAD_URL, "https://a14e7756b1d6938002e88cc55d3870de.dd.cdntips.com/imtt.dd.qq.com/16891/apk/B5DF5905B2369B8051A477F8A48A2E35.apk?mkey=5ee9949e7d4669c0&f=0c2f&fsname=com.tencent.qqpinyin_7.2.3_5524.apk&csr=1bbd&cip=125.70.79.53&proto=https")
                                .putString(DownWorker.DOWNLOAD_FILE_PATH,getExternalFilesDir(null)!!.absolutePath + File.separator + "Apk" + File.separator + "test.apk")
                                .build())
                        .build()
                WorkManager.getInstance(this@SettingActivity).getWorkInfoByIdLiveData(worker.id)
                        .observe(this, Observer {
                            if (it.state == WorkInfo.State.ENQUEUED) {
                                Log.e("Setting", "任务入队======")
                            }
                            if (it.state == WorkInfo.State.RUNNING) {
                                Log.e("Setting", "任务正在执行======progress: ${it.progress.getInt(DownWorker.CURRENT_PROGRESS, 0)}")
                            }
                            if (it.state.isFinished && 1 == it.outputData.getInt(DownWorker.DOWNLOAD_STATE, 0)) {
                                Log.e("Setting", "任务完成======结果: ${it.outputData.getInt(DownWorker.DOWNLOAD_STATE, 0)}")
                                // 跳转安装界面
                                installPackage(FileUtil.createFile(getExternalFilesDir(null)!!.absolutePath + File.separator + "Apk" + File.separator + "test.apk"))
                            }
                        })

                WorkManager.getInstance(this@SettingActivity).enqueue(worker)

 */

class DownWorker(val context: Context, workerParams: WorkerParameters) : ListenableWorker(context, workerParams) {
    companion object {
        val CURRENT_PROGRESS = "current_progress"
        val DOWNLOAD_STATE = "download_state"
        val DOWNLOAD_URL = "download_url"
        val DOWNLOAD_FILE_PATH = "download_file_path"
    }

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer: CallbackToFutureAdapter.Completer<Result> ->
            if (inputData.getString(DOWNLOAD_URL).isNullOrEmpty()){
                throw IllegalArgumentException("下载地址不能为空")
            }
            val downloadUrl = inputData.getString(DOWNLOAD_URL)
            if (inputData.getString(DOWNLOAD_FILE_PATH).isNullOrEmpty()) {
                throw IllegalArgumentException("文件保存路径不能为空")
            }
            val file = FileUtil.createFile(inputData.getString(DOWNLOAD_FILE_PATH)!!)

            var call: Call<ResponseBody>? = null

            val listener = object :
                JDownloadListener {
                override fun onComplete() {
                    completer.set(Result.success(Data.Builder().putInt(DOWNLOAD_STATE, 1).build()))
                }

                override fun onFail(errorInfo: String?) {
                    completer.set(Result.failure())
                }

                override fun onStartDownload(totalLength: Long) {
                    setProgressAsync(Data.Builder().putInt(CURRENT_PROGRESS, -1).build())
                }

                override fun onProgress(progress: Int) {
                    setProgressAsync(Data.Builder().putInt(CURRENT_PROGRESS, progress).build())
                }
            }
            val client = OkHttpClient.Builder()
                    .addInterceptor(
                        JDownloadInterceptor(
                            listener
                        )
                    )
                    .retryOnConnectionFailure(true)
                    .connectTimeout(5000, TimeUnit.SECONDS)

            val retrofit = Retrofit.Builder()
                    .client(client.build())
                    .baseUrl("http://dldir1.qq.com/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

            // 取消任务
            completer.addCancellationListener(Runnable { call?.cancel() }, Executors.newSingleThreadExecutor())

            Thread(Runnable {
                call = retrofit.create(DownloadService::class.java).downloadApp(downloadUrl!!)
                call!!.execute().body()?.let {
                    writeFile(it.byteStream(), file)
                }
            }).start()

        }

    }

    /**
     * 将输入流写入文件
     * @param inputStream
     * @param file
     */
    private fun writeFile(inputStream: InputStream, file: File) {
        if (file.exists()) {
            file.delete()
        }
        file.outputStream().use { out ->
            inputStream.use {
                it.copyTo(out)
            }
        }
    }

}

//    override fun doWork(): Result {
//    CoroutineScope(SupervisorJob()).launch(
//    CoroutineExceptionHandler { _, e ->
//        Log.e("Settiing", "launch=========${e.message}")
//        mState = -1
//    }
//    ) {
//        withContext(Dispatchers.IO) {
//            val body = mRetrofit.create(DownloadService::class.java).downloadApp(mDownloadUrl)
//            Log.e("Settiing", "body=========${body.contentLength()}")
//            mState = 1
//        }
//    }

//        return try {
//          val call =  mRetrofit.create(DownloadService::class.java).downloadApp(mDownloadUrl)
//            call.enqueue(object :Callback<ResponseBody>{
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Log.e("Settiing", "onFailure=========${t.message}")
//
//                }
//
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    Log.e("Settiing", "onResponse=========${response.body()?.contentLength()}")
//
//                }
//            })
//            Result.success()
//        } catch (e: Exception) {
//            Log.e("Settiing","launch=========${e.message}")
//            Result.failure()
//        }
//        try {
//            //模拟耗时任务
//            Thread.sleep(3000)
//            return Result.success(Data.Builder().let {
//                        it.putInt("code",200)
//                    it.build()
//            })
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//            return Result.failure()
//        }
//
//    }