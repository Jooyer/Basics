package cn.lvsong.lib.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.lvsong.lib.library.utils.click
import cn.lvsong.lib.library.utils.withTrigger
import cn.lvsong.lib.ui.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_custom2.*

class CustomActivity2 : BaseActivity() {

    override fun needUseImmersive() = 1

    override fun getLayoutId() = R.layout.activity_custom2

    override fun setLogic() {


    }

    override fun bindEvent() {
        cv1.withTrigger().click {
            Toast.makeText(this@CustomActivity2,"点击了叉叉", Toast.LENGTH_SHORT).show()
        }

        cv2.withTrigger().click {
            Toast.makeText(this@CustomActivity2,"点击了带圈叉叉", Toast.LENGTH_SHORT).show()
        }

        window.decorView.postDelayed({
            cdv.startCountDown(5)
        },1200L)

    }

    fun onStartRecord(view: View) {
        mbv_audio_record.startRecord()
    }
    fun onStopRecord(view: View) {
        mbv_audio_record.stopRecord()
    }
    fun onPlayRecord(view: View) {
        mbv_audio_record.playRecord()
    }
    fun onResetRecord(view: View) {
        mbv_audio_record.resetRecord()
    }

}