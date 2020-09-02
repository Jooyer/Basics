package cn.lvsong.lib.ui

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * https://blog.csdn.net/NJP_NJP/article/details/103524778
 * 初始化方式:
 * ViewModelProvider(this).get(NetModel::class.java)
 */
open class BaseViewModel(@NonNull application: Application) : AndroidViewModel(application) {
    // 储存网络请求状态
    val mLoadState = MutableLiveData<LoadState>()



}