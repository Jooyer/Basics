package cn.lvsong.lib.demo

import android.view.View
import cn.lvsong.lib.demo.databinding.ActivityEncodeOrDecodeBinding
import cn.lvsong.lib.net.utils.AESUtil
import cn.lvsong.lib.ui.BaseActivity

/**
 * 网络请求加密解密
 */
class EncodeOrDecodeActivity : BaseActivity<ActivityEncodeOrDecodeBinding>() {

    private val data = """{"name":"张三","age":20,"sex":"男"}"""
    // 服务器返回的
    private val publicRSAKey =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj56tVJIzpkddcTKu zsuKMcyq+tmdYY74EF08HbqX9Rd/z9q2VvyUZj7hs3icwdbZP3sYZV9/IMq4 4tDlARlW/dEpb/tovo35Fiaq26oREXbkD/jSPyhdQv22oGfMZrPxeJ/Qcp0h +VyIDtIYP6v1cuSSAwglcMUvYhIvRq5ntjuyimrWV/wxTynTqK5QXOIFM8aG pbO1r4MTYsnzXSJDgbGjAQnCDcrsUjDhq/R8sgtqDbFUFsFUIGIP3X5ulQJ4 SaLVw0zwp+82RlHUfcHS6S1nbh53SDpG/id5QSGCbzR+BpqRIlysV3kzWmKZ DpOXBu8mqNK5vcof0geyC4xbBQIDAQAB"
    // 对称加密操作
    private val key = AESUtil.generateSecretKey("Common")


    override fun needUseImmersive() = 1
    // 根据需要重写
    override fun getStatusBarColor() = android.R.color.holo_green_light

    override fun getLayoutId() = R.layout.activity_encode_or_decode

    override fun getViewBinging(view: View): ActivityEncodeOrDecodeBinding {
        return ActivityEncodeOrDecodeBinding.bind(view)
    }

    override fun setLogic() {

    }

    override fun bindEvent() {
    }




}
