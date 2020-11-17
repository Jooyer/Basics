package cn.lvsong.lib.demo.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.lvsong.lib.demo.data.Data
import cn.lvsong.lib.demo.util.NetUtil
import cn.lvsong.lib.ui.BaseViewModel
import cn.lvsong.lib.ui.LoadState
import cn.lvsong.lib.ui.launch
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * Desc:
 * Author: Jooyer
 * Date: 2020-08-29
 * Time: 22:07
 */
class NetModel(@NonNull application: Application) : BaseViewModel(application) {

    val mListData = MutableLiveData<List<Data>>()

    fun getData(page:Int){
        launch({
            //更新加载状态
            mLoadState.value = LoadState.Loading()
            // 请求数据
            val data = NetUtil.apiService.getList(page)
            mListData.value = data.data?.datas
            //更新加载状态
            mLoadState.value = LoadState.Success(code = data.errorCode,type = 1)
        },{
            //加载失败的状态
            mLoadState.value = LoadState.Failure(it.message ?: "加载失败")
        })
    }


    fun getDetail(){
        launch({
            //更新加载状态
            mLoadState.value = LoadState.Loading()
            // 请求数据
            ////////////////////
            ////////////////////  具体略,参考上面 getData
            ////////////////////

            //更新加载状态
            mLoadState.value = LoadState.Success(type = 2)
        },{
            //加载失败的状态
            mLoadState.value = LoadState.Failure(it.message ?: "加载失败")
        })
    }

}