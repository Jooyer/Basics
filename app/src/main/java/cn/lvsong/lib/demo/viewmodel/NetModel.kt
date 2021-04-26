package cn.lvsong.lib.demo.viewmodel

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.lvsong.lib.demo.data.Data
import cn.lvsong.lib.demo.util.NetUtil
import cn.lvsong.lib.net.network.NetWorkMonitor
import cn.lvsong.lib.net.network.NetworkType
import cn.lvsong.lib.ui.BaseViewModel
import cn.lvsong.lib.ui.LoadState
import cn.lvsong.lib.ui.launch
import kotlinx.coroutines.*

/**
 * Desc:
 * Author: Jooyer
 * Date: 2020-08-29
 * Time: 22:07
 */
class NetModel(@NonNull application: Application) : BaseViewModel(application) {

    val mListData = MutableLiveData<List<Data>>()

    private var mJob:Job? = null

    fun getData(page: Int) {
        mJob= launch({
            //更新加载状态
            mLoadState.value = LoadState.Loading()
            // 请求数据
            val data = async { NetUtil.apiService.getList(page) }
            val info = data.await()
            mListData.value = info.data?.datas
            //更新加载状态
            mLoadState.value = LoadState.Success(code = info.errorCode,apiType = 1 )
        }, {
            //加载失败的状态
            mLoadState.value = LoadState.Failure(apiType = 1,msg = it.message ?: "加载失败")
        })
    }


    fun getDetail() {
        launch({
            //更新加载状态
            mLoadState.value = LoadState.Loading()
            // 请求数据
            ////////////////////
            ////////////////////  具体略,参考上面 getData
            ////////////////////

            //更新加载状态
            mLoadState.value = LoadState.Success(code = 200,apiType = 2,) // code 也可以统一处理
        }, {
            //加载失败的状态
            mLoadState.value = LoadState.Failure(apiType = 2,msg = it.message ?: "加载失败")
        })
    }

    @NetWorkMonitor([NetworkType.NETWORK_4G, NetworkType.NETWORK_WIFI, NetworkType.NETWORK_NONE])
    fun onNetWorkStateChange(state: NetworkType) {
        if (NetworkType.NETWORK_NONE == state){
            mJob?.cancel(object :CancellationException(){
                override fun fillInStackTrace(): Throwable {
                    mLoadState.postValue(LoadState.Failure(apiType = 1, "取消加载"))
                    return super.fillInStackTrace()
                }
            })
        }
    }

}