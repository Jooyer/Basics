package cn.lvsong.lib.net.cover

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/** https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/api/ApiResponse.kt
 * @Author 刘一召
 * @Date  2019/2/21
 * @Time 18:23
 */
class LiveDataAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<R>> {
    override fun adapt(call: Call<R>): LiveData<R> {
        return object :LiveData<R>(){
            // 在多线程中,业务处理的线程安全,保证单一线程作业
            val started = AtomicBoolean(false)

            override fun onActive() {
               if (started.compareAndSet(false,true)){
                   call.enqueue(object : Callback<R>{
                       override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(response.body())
                       }
                       override fun onFailure(call: Call<R>, t: Throwable) {
                            postValue(null)
                       }
                   })
               }
            }
        }
    }

    override fun responseType(): Type {
        return responseType
    }
}