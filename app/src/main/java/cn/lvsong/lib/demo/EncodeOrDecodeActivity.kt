package cn.lvsong.lib.demo

import android.view.View
import cn.lvsong.lib.demo.databinding.ActivityEncodeOrDecodeBinding
import cn.lvsong.lib.library.listener.OnClickFastListener
import cn.lvsong.lib.net.utils.AESUtil
import cn.lvsong.lib.net.utils.Base64Util
import cn.lvsong.lib.net.utils.RSAUtil
import cn.lvsong.lib.ui.BaseActivity
import cn.lvsong.lib.ui.BaseViewModel

/**
 * 网络请求加密解密
 */
class EncodeOrDecodeActivity : BaseActivity<ActivityEncodeOrDecodeBinding, BaseViewModel>() {

    private val data = """
        [
            {"name":"张三","age":20,"sex":"男","add":"xx省x山市x北区xx小区"},
            {"name":"李四","age":22,"sex":"男","add":"xx省x山市x北区xx小区"},
            {"name":"王五","age":22,"sex":"男","add":"xx省x山市x北区xx小区"},
            {"name":"赵六","age":21,"sex":"男","add":"xx省x山市x北区xx小区"},
            {"name":"麻七","age":20,"sex":"男","add":"xx省x山市x北区xx小区"}
        ]
    """.trimIndent()

    // 公钥
    private val sPublicKey = """
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq1dTHs5o75VR6WxGAoJZ
        B+cNO362DOnxgyuDH+XUo1tW/sIfrzRLNig/OU1wZtWdY9hZcPosqVV7R3wBIKO4
        6tjK4DVKFt95Zq+dnlE4HTi+NRKdM9yGhMtGwIiWlI2NPolt9Gc0HeXMIpBUYJ3r
        Ag0XID4mUqO6HTrrislI0vqjHrnRay8z/kCsvtynjs6t6VERmQ699ywq/mKXhlD7
        ea4L4mte9ja6hPC3B9AlASFTxUiFhnIu9ni6KGV+RPybFdz4C+gLxXJVRkhvBVNW
        JX82iryztPhMutEgsRNun10kEWJyeocQcLvAgVuZQYa5v+AKhyc47iFbsJxaGMKO
        qQIDAQAB
        """.trimIndent()

    // 私钥, 实际上私钥存放在服务器,这里为了演示效果
    private val sPrivateKey = """
        MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCrV1MezmjvlVHp
        bEYCglkH5w07frYM6fGDK4Mf5dSjW1b+wh+vNEs2KD85TXBm1Z1j2Flw+iypVXtH
        fAEgo7jq2MrgNUoW33lmr52eUTgdOL41Ep0z3IaEy0bAiJaUjY0+iW30ZzQd5cwi
        kFRgnesCDRcgPiZSo7odOuuKyUjS+qMeudFrLzP+QKy+3KeOzq3pURGZDr33LCr+
        YpeGUPt5rgvia172NrqE8LcH0CUBIVPFSIWGci72eLooZX5E/JsV3PgL6AvFclVG
        SG8FU1YlfzaKvLO0+Ey60SCxE26fXSQRYnJ6hxBwu8CBW5lBhrm/4AqHJzjuIVuw
        nFoYwo6pAgMBAAECggEACx+4wEZuA/v28kd6xh1DUs2FNj3Jlk4L8CmqMi8gM9jc
        GDRvN9dBI3YlIKnD6JE+ziV/gmO49DzIq6vugM7QVG2YbPUDZzfBWybUqLDPiqoi
        mIcprfJyc1BP5XPDXvKlCbsvaHouJTDET4wnm5GKbpAoq7RilY6WqIt+O6efcumt
        QQTSX65JhQDXdxYRntr1ysRCDxSZ7W3g1xWN+dDyvpiujNhV4E/9N1SyTZDBjnzG
        snKJa5sgYaTGntfMggsyryzOnSWhwvgw5X5Dxgc/wgKev2XztYt7fZ9c1ZgmddQV
        DeldCYdoxUpmdC23FxBSIT9ROlZ5LIguPbRxDY5PnwKBgQDV+kS6CfixpDioYqpl
        lDc6o4C7GIknPJgbrUgXI0Pm4e+kx7QfD1NJwoyHpUNesjsheTnhixqhzG4k6mf5
        4sEs/tK9BlpqkyqDUftxggqem2qvGa5QYYeYRi2RGrdMEvBhAobvJ/FVfKfUIQWK
        8P7QBEyw0UBrnDIyZpz6bICd/wKBgQDM/YE7P8KvmYkoNsKtBTI4fv/Zoqq7N9uD
        B60awLcmAiStd8fh6ZBeTIea9SSWnw/Cu/k6pq80jWCdoNbb3aReDZq0M1Xhax78
        I+cxI2/WmZctCjBdo+6Xh3MrVPWeCtU3+5GkI9vGzebVaGseQxbHEo9cYO8Qcib9
        X8bpYo0jVwKBgH2STVrQFz5InWdT36OVA4RKB3XExFheWvWDD1dkvqhz1urgt3ey
        yrHylf3UomCkP8c5GRqfQ5XIHqtTVIl05OC++nUrkUwR6VT/e4v1QL4rfsQbUgyh
        760RIhSRlRfz1VVY4k7fJeWBLgxzBImxPBhfLZJmemOsWljxlxXvLtuJAoGBAKoc
        jcwPY465yTqYBwHyV/TOX5/KAAkRl5arHhofZ12hVbKmvjdZzjaTJ82ONejQ4xFl
        ULQ9cxQXZuAog2U+D5MFkyaLm29PqAzx9n+uurbKCsyTsgNjcTJKc2a4QIQA1WR5
        3ijqoQ3f/PmIh6w7XIGjg1jUEFADixulCbrPz/RNAoGAAagWWPMHtKPTWC8mRZW+
        UnFs+PXS1BSeYjEavidPLXBejPkTjm43Kx/1GX9OiBHgvUtQ93WZvCdaPWNCBbZp
        LUXdD+jeOzRebiremEUm/23VXICge7ul2hW5OfhZw643xWwswL6kcFEmEgxQCeDf
        XRNzYKYAe1Z3SOqfZStVIlo=
        """.trimIndent()

    // 对称加密操作
    private val key = AESUtil.generateSecretKey("Basic") // 实际使用中这里需要用一个长一些的字符串

    private var mData = ""

    private var mAes = ""

    override fun needUseImmersive() = 1

    // 根据需要重写
    override fun getStatusBarColor() = android.R.color.holo_green_light

    override fun getLayoutId() = R.layout.activity_encode_or_decode

    override fun getViewBinging(view: View): ActivityEncodeOrDecodeBinding {
        return ActivityEncodeOrDecodeBinding.bind(view)
    }

    override fun setLogic() {
        mBinding?.tvShowResult?.text = key
    }

    override fun bindEvent() {

        // 1. AES加密数据
        mBinding?.tvAesEncrypt?.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {
                mData = AESUtil.encrypt(data, key)
                mBinding?.tvShowResult?.text = mData
            }

        })

        // 2. Java方法加密AES密钥
        mBinding?.tvJavaEncrypt?.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {
                mAes = Base64Util.encodeToString(RSAUtil.encryptByPublicKey(Base64Util.decodeFromString(key), Base64Util.decodeFromString(sPublicKey)))
                mBinding?.tvShowResult?.text = mAes
            }

        })
        // 3. JNI方法加密AES密钥(SO引入导致库大了3M,暂未引入)
        mBinding?.tvNativeEncrypt?.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {

            }

        })
        // 4. JNI方法解密AES密钥(SO引入导致库大了3M,暂未引入)
        mBinding?.tvNativeDecrypt?.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {

            }

        })
        // 5. Java方法解密AES密钥
        mBinding?.tvJavaDecrypt?.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {
                mAes = Base64Util.encodeToString(RSAUtil.decryptByPrivateKey(Base64Util.decodeFromString(mAes), Base64Util.decodeFromString(sPrivateKey)))
                mBinding?.tvShowResult?.text = mAes
            }

        })
        // 6. AES解密数据
        mBinding?.tvAesDecrypt?.setOnClickListener(object : OnClickFastListener() {
            override fun onFastClick(v: View) {
                mData = AESUtil.decrypt(mData, mAes)
                mBinding?.tvShowResult?.text = mData
            }

        })

    }


}
