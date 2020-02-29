package cn.lvsong.lib.demo.presenter

import androidx.lifecycle.LifecycleOwner
import cn.lvsong.lib.demo.LazyFragment
import cn.lvsong.lib.demo.model.LazyModel
import cn.lvsong.lib.ui.mvp.BasePresenter

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-11-09
 * Time: 23:39
 */
class LazyPresenter(view: LazyFragment) : BasePresenter<LazyFragment, LazyModel>(view,LazyModel()) {

    override fun onCreate(provider: LifecycleOwner) {
       mModel.testRunOnCreateMethod()
    }

}