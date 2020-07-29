package cn.lvsong.lib.ui.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel :ViewModel() {
    // 储存网络请求状态
    val mLoadState = MutableLiveData<LoadState>()




}