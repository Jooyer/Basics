package cn.lvsong.lib.demo

import android.view.View
import android.widget.Toast
import cn.lvsong.lib.demo.databinding.ActivityCustomToolbarBinding
import cn.lvsong.lib.ui.BaseActivity

/**
 * 展示自定义Toolbar
 */
class CustomToolbarActivity : BaseActivity<ActivityCustomToolbarBinding>() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom_toolbar

    override fun getViewBinging(view: View): ActivityCustomToolbarBinding {
        return ActivityCustomToolbarBinding.bind(view)
    }

    override fun setLogic() {


    }

    override fun bindEvent() {
        mBinding?.ct1?.setRightImageListener(View.OnClickListener {
            Toast.makeText(this,"点击右侧图片",Toast.LENGTH_SHORT).show()
            mBinding?.ct1?.alpha = 0.5f
        })

        mBinding?.ct2?.setRightImageListener(View.OnClickListener {
            Toast.makeText(this,"点击右侧第一个图片",Toast.LENGTH_SHORT).show()
        })

        mBinding?.ct2?.setRightImage2Listener(View.OnClickListener {
            Toast.makeText(this,"点击右侧倒数第二个图片",Toast.LENGTH_SHORT).show()
        })

        mBinding?.ct3?.setMoreViewListener(View.OnClickListener {
            Toast.makeText(this,"点击了更多按钮",Toast.LENGTH_SHORT).show()
        })

        mBinding?.ct12?.setRightImageListener(View.OnClickListener {
//            ct_13.setRightImageChecked(!ct_13.getRightImageChecked())
            if (mBinding!!.ct12.getRightImageChecked()) {
                mBinding?.ct12?.setRightImageDrawable(R.drawable.normal)
            }else{
                mBinding?.ct12?.setRightImageCheckedDrawable(R.drawable.select)
            }
        })

        mBinding?.ct12?.setRightImage2Listener(View.OnClickListener {
//            ct_13.setRightImage2Checked(!ct_13.getRightImage2Checked())
            if (mBinding!!.ct12.getRightImage2Checked()) {
                mBinding?.ct12?.setRightImage2Drawable(R.drawable.normal)
            }else{
                mBinding?.ct12?.setRightImage2CheckedDrawable(R.drawable.select)
            }
        })

    }


}