package cn.lvsong.lib.demo

import android.util.Log
import android.view.View
import android.widget.Toast
import cn.lvsong.lib.demo.databinding.ActivityCustom2Binding
import cn.lvsong.lib.library.utils.click
import cn.lvsong.lib.library.utils.withTrigger
import cn.lvsong.lib.ui.BaseActivity

class CustomActivity2 : BaseActivity<ActivityCustom2Binding>() {

    private var mCurChoicePlace = ""

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom2

    override fun getViewBinging(view: View): ActivityCustom2Binding {
        return ActivityCustom2Binding.bind(view)
    }

    override fun setLogic() {
        setAddress()
    }

    override fun bindEvent() {
        mBinding?.cv1?.withTrigger()?.click {
            Toast.makeText(this@CustomActivity2, "点击了叉叉", Toast.LENGTH_SHORT).show()
        }

        mBinding?.cv2?.withTrigger()?.click {
            Toast.makeText(this@CustomActivity2, "点击了带背景叉叉", Toast.LENGTH_SHORT).show()
        }


        mBinding?.cv3?.withTrigger()?.click {
            Toast.makeText(this@CustomActivity2, "点击了带背景和圈叉叉", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setAddress() {
        val data = arrayListOf<String>(*resources.getStringArray(R.array.province_info))
        mCurChoicePlace = data[3]
        mBinding?.pickerView?.apply {
            setData(data)
            setSelected(3)
            setOnSelectListener { text ->
                Log.e("Login", "======== $text");
            }
        }

    }


    fun onStartRecord(view: View) {
        mBinding?.mbvAudioRecord?.startRecord()
    }

    fun onStopRecord(view: View) {
        mBinding?.mbvAudioRecord?.stopRecord()
    }

    fun onPlayRecord(view: View) {
        mBinding?.mbvAudioRecord?.playRecord()
    }

    fun onResetRecord(view: View) {
        mBinding?.mbvAudioRecord?.resetRecord()
    }

}