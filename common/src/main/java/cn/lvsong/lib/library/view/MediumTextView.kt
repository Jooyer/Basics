package cn.lvsong.lib.library.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *
 * @ProjectName:    android
 * @Package:        cn.lvsong.lib.library.view
 * @ClassName:      MediumTextView
 * @Description:    接近于PingFang SC Medium的效果的TextView
 * @Author:         Jooyer
 * @CreateDate:     2020/6/8 10:38
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 */
class MediumTextView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null)

    init {
        paint.isFakeBoldText = true
    }

}