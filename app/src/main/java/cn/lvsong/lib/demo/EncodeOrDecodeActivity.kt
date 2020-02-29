package cn.lvsong.lib.demo

import android.util.Log
import android.view.View
import cn.lvsong.lib.demo.api.ApiService
import cn.lvsong.lib.demo.presenter.EncodeOrDecodePresenter
import cn.lvsong.lib.net.retrofit.RxRetrofit
import cn.lvsong.lib.net.utils.AESUtil
import cn.lvsong.lib.net.utils.Base64Util
import cn.lvsong.lib.net.utils.RSAUtil
import cn.lvsong.lib.net.utils.RxObserver
import cn.lvsong.lib.ui.mvp.BaseActivity
import io.reactivex.disposables.Disposable

/**
 * 网络请求加密解密
 */
class EncodeOrDecodeActivity : BaseActivity<EncodeOrDecodePresenter>() {

    private val data = """{"name":"张三","age":20,"sex":"男"}"""
    // 服务器返回的
    private val publicRSAKey =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj56tVJIzpkddcTKu zsuKMcyq+tmdYY74EF08HbqX9Rd/z9q2VvyUZj7hs3icwdbZP3sYZV9/IMq4 4tDlARlW/dEpb/tovo35Fiaq26oREXbkD/jSPyhdQv22oGfMZrPxeJ/Qcp0h +VyIDtIYP6v1cuSSAwglcMUvYhIvRq5ntjuyimrWV/wxTynTqK5QXOIFM8aG pbO1r4MTYsnzXSJDgbGjAQnCDcrsUjDhq/R8sgtqDbFUFsFUIGIP3X5ulQJ4 SaLVw0zwp+82RlHUfcHS6S1nbh53SDpG/id5QSGCbzR+BpqRIlysV3kzWmKZ DpOXBu8mqNK5vcof0geyC4xbBQIDAQAB"
    // 对称加密操作
    private val key = AESUtil.generateSecretKey("Common")


    override fun needUseImmersive() = true
    // 根据需要重写
    override fun getStatusBarColor() = android.R.color.holo_green_light

    override fun getLayoutId() = R.layout.activity_encode_or_decode

    override fun setLogic() {

    }

    override fun bindEvent() {
    }

    fun onEncode(view: View) {
        Log.e("Test", "key==========$key")
        // 加密数据
        val encrypt = AESUtil.encrypt(data, key)
        // 加密 AES 的 key
        val aesKey = RSAUtil.encryptByPublicKey(key.toByteArray(), Base64Util.decode(publicRSAKey))
        Log.e("Test", "json==========$aesKey --- $encrypt")
        RxRetrofit.instance.getApi(ApiService::class.java)
            .test(Base64Util.encode(aesKey), encrypt)
            .compose(RxRetrofit.instance.maybeTransformer())
            .subscribe(object : RxObserver<String>(){
                override fun success(t: String) {
                    Log.e("Test", "onSuccess=========data:  $t")
                    decryptStr(t)
                }

                override fun getDisposable(d: Disposable) {
                    Log.e("Test", "getDisposable==========${d.isDisposed}")
                }

                override fun error(code: Int, msg: String) {
                    Log.e("Test", "onError==========$code")
                }

            })
    }

    // 解密数据
    private fun decryptStr(data: String): String {
        // 1. 解密AES
        val aesKey = RSAUtil.decryptByPublicKeyForSpilt(key, Base64Util.decode(publicRSAKey))
        // 2. 解密数据
        val decrypt = AESUtil.decrypt(data, aesKey)
        Log.e("Encode","decryptStr========= $decrypt")
        return decrypt
    }
}
