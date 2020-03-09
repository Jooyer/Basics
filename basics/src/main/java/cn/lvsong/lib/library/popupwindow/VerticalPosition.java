package cn.lvsong.lib.library.popupwindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-05-26
 * Time: 9:01
 */
@IntDef({
        VerticalPosition.CENTER,
        VerticalPosition.ABOVE,
        VerticalPosition.BELOW,
        VerticalPosition.ALIGN_TOP,
        VerticalPosition.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface VerticalPosition {
    int CENTER = 0;
    int ABOVE = 1;
    int BELOW = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
