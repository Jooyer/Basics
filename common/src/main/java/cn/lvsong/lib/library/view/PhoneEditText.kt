package cn.lvsong.lib.library.view

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

/**
 * @ProjectName: android
 * @ClassName: PhoneEditText
 * @Description: 实现自定义手机号输入框，手机号码效果为344效果，例如111 1111 1111
 * @Author: Jooyer
 * @CreateDate: 2020/5/30 17:13
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 */
/*

    <cn.lvsong.lib.library.view.PhoneEditText
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_50"
        android:layout_margin="@dimen/padding_20"
        android:background="@color/color_EEEEEE"
        android:hint="请输入电话号码"
        android:paddingStart="@dimen/padding_10"
        android:paddingEnd="@dimen/padding_10"
        android:textColor="@color/color_333333"
        android:textColorHint="@color/color_666666" />

 */
class PhoneEditText : AppCompatEditText, TextWatcher {

    // 特殊下标位置
    private val PHONE_INDEX_3 = 3
    private val PHONE_INDEX_4 = 4
    private val PHONE_INDEX_8 = 8
    private val PHONE_INDEX_9 = 9

    private var preCharSequence: String? = null
    private var onPhoneEditTextChangeListener: OnPhoneEditTextChangeListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView()
    }


    private fun initView() {
        //设置输入过滤器
        filters = arrayOf(
            InputFilter { source, start, end, spanned, dstart, dend -> //在onTextChanged方法里执行setText(sb.toString());会到这里，内容一样直接返回
                if (TextUtils.equals(source, preCharSequence)) {
                    return@InputFilter null
                }
                //过滤掉空格和换行，dstart为13表示光标位置是11位数字+2个空格时，返回空字符
                if (" " == source.toString() || source.toString().contentEquals("\n")
                    || dstart == 13
                ) {
                    ""
                } else if (phoneText.length == 11) {
                    ""
                } else {
                    null
                }
            },
            InputFilter { source, start, end, dest, dstart, dend -> //过滤掉所有的特殊字符，这里的字母只过滤掉了wp，因为在众多机型测试时，只能输入这两个，如果不放心可以添加a-z所有字母
                val speChat =
                    "[`~!@#$%^&*()+\\-=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？a-zA-Z]"
                val pattern = Pattern.compile(speChat)
                val matcher = pattern.matcher(source.toString())
                if (matcher.find()) {
                    ""
                } else {
                    null
                }
            }
        )
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        super.onTextChanged(s, start, before, count)
        if (TextUtils.equals(preCharSequence, s)) {
            return
        }
        if (null != onPhoneEditTextChangeListener) {
            onPhoneEditTextChangeListener!!.onTextChange(phoneText.length == 11, phoneText)
        }
        if (null == s || s.isEmpty()) {
            return
        }
        val sb = StringBuilder()
        for (i in s.indices) {
            if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s[i] == ' ') {
                continue
            } else {
                sb.append(s[i])
                if ((sb.length == PHONE_INDEX_4 || sb.length == PHONE_INDEX_9)
                    && sb[sb.length - 1] != ' '
                ) {
                    sb.insert(sb.length - 1, ' ')
                }
            }
        }

        //这里主要处理添加空格后的字符串，before=0为输入字符，before=1为删除字符，将光标移动到正确的位置
        if (sb.toString() != s.toString()) {
            var index = start + 1
            if (sb[start] == ' ') {
                if (before == 0) {
                    index++
                } else {
                    index--
                }
            } else {
                if (before == 1) {
                    index--
                }
            }
            preCharSequence = sb.toString()
            setText(sb.toString())
            //对setSelection添加异常捕获，防止出现意外的IndexOutOfBoundsException异常
            try {
                setSelection(index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun afterTextChanged(s: Editable) {}
    private fun replaceBlank(str: String?): String {
        var dest = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            if (m.find()) {
                dest = m.replaceAll("")
            }
        }
        return dest
    }


    interface OnPhoneEditTextChangeListener {
        /**
         * 对外提供接口监听
         *
         * @param phoneNum 字符串
         * @param isEleven 现在是否是11位数字
         */
        fun onTextChange(isEleven: Boolean, phoneNum: String?)
    }

    /**
     * 设置输入变化监听
     */
    fun setOnPhoneEditTextChangeListener(listener: OnPhoneEditTextChangeListener?) {
        onPhoneEditTextChangeListener = listener
    }

    /**
     * 获得不包含空格的电话号码
     */
    val phoneText: String
        get() {
            val str = text.toString()
            return replaceBlank(str)
        }
}