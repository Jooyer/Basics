package cn.lvsong.lib.demo.presenter

import cn.lvsong.lib.demo.StatusActivity
import cn.lvsong.lib.demo.model.StatusModel
import cn.lvsong.lib.ui.mvp.BasePresenter

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-11-10
 * Time: 10:11
 */
class StatusPresenter(view: StatusActivity) :
    BasePresenter<StatusActivity, StatusModel>(view,StatusModel()) {

    fun test(){

    }

}