package cn.lvsong.lib.library.listener

import android.text.Editable
import android.text.TextWatcher

/**
 * Desc: TextWatcher 默认实现类
 * Author: Jooyer
 * Date: 2018-08-11
 * Time: 14:30
 */
open class EditTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {

    }
}
