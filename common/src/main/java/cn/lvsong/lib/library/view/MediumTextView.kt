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

/**
 * 在xml文件中使用Android:textStyle="bold" 可以将英文设置成粗体，但是不能将中文设置成粗体。
将中文设置成粗体的方法是：

1.TextView tv = (TextView)findViewById(R.id.TextView01);
TextPaint tp = tv.getPaint();
tp.setFakeBoldText(true);

2.要取消加粗效果的话可以像下面这样设置
TextPaint tp = tv.getPaint();
tp.setFakeBoldText(false);

 */

class MediumTextView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
    AppCompatTextView(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null)

    init {
        paint.isFakeBoldText = true
    }

}