package cn.lvsong.lib.demo

import android.view.View
import cn.lvsong.lib.demo.databinding.ActivityCustom4Binding
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.ui.BaseActivity
import cn.lvsong.lib.ui.BaseViewModel

class CustomActivity4 : BaseActivity<ActivityCustom4Binding, BaseViewModel>() {

    override fun getLayoutId() = R.layout.activity_custom4

    override fun getViewBinging(view: View): ActivityCustom4Binding {
        return ActivityCustom4Binding.bind(view)
    }

    override fun needUseImmersive() = 1

    override fun setLogic() {
        window.decorView.postDelayed({
            mBinding?.cdv?.startCountDown(5)
        },1200L)

        mBinding?.etvTextView?.initWidth(DensityUtil.getRealWidth(this)  - DensityUtil.dp2pxRtInt(40))

        mBinding?.etvTextView?.setOriginalText("据海关总署网站9月18日消息，因从印度尼西亚进口1批冻带鱼1个外包装样本检出新冠病毒核酸阳性，根据海关总署公告2020年第103号的规定，全国海关自即日起暂停接受印度尼西亚PT.PUTRI INDAH（注册编号为CR010-02）水产品生产企业产品进口申报1周，期满后自动恢复。")
//        etv_text_view.setOriginalText("From now on, regularly do this exercise instead. Spin an uplifting story that runs like a movie script. Some visualization will be helpful. You build on a story with a positive outline. The longer you can tell this story to yourself the better. It is also best if you can make this story one about having all your goals achieved. When you do this, you start to internalize your goals and dreams, as if they are something that you have already achieved.")


    }

    override fun bindEvent() {
        mBinding?.cdv?.setOnClickListener {
            mBinding?.vavTest?.startAnim()
        }
    }



}