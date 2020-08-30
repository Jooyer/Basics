package cn.lvsong.lib.demo.viewmodel

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
class NetModel : BaseViewModel() {

    val mListData = MutableLiveData<List<Data>>()

    fun getData(){

        launch({
            //更新加载状态
            mLoadState.value = LoadState.Loading()
            // 请求数据
            val data = NetUtil.apiService.getList(1)
            mListData.value = data.data?.datas
            //更新加载状态
            mLoadState.value = LoadState.Success()
        },{
            //加载失败的状态
            mLoadState.value = LoadState.Failure(it.message ?: "加载失败")
        })
    }

}