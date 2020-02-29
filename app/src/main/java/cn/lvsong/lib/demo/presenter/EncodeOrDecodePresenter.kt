package cn.lvsong.lib.demo.presenter

import cn.lvsong.lib.demo.EncodeOrDecodeActivity
import cn.lvsong.lib.demo.model.EncodeOrDecodeModel
import cn.lvsong.lib.ui.mvp.BasePresenter

/**
 * Desc:
 * Author: Jooyer
 * Date: 2020-01-10
 * Time: 11:22
 */
class EncodeOrDecodePresenter(view: EncodeOrDecodeActivity) :
    BasePresenter<EncodeOrDecodeActivity, EncodeOrDecodeModel>(
        view,
        EncodeOrDecodeModel()
    ) {



}