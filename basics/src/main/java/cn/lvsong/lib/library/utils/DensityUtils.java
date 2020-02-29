package cn.lvsong.lib.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * px与dp之间的转化
 * Created by Jooyer on 2016/4/18
 */
public class DensityUtils {

    /**
     * dp -> px
     * @param dp
     * @return
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 获取屏幕尺寸
     */
    public static DisplayMetrics getWindowSize(Context context){
        return context.getResources().getDisplayMetrics();
    }


    /**
     * 获取当前屏幕截图，不包含状态栏
     * @param activity
     * @return bp
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.getWidth(), bmp.getHeight() - statusBarHeight);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bp;
    }


//    public static void setMargin(Context context,TabLayout  tab_business){
//        Class<?> tabLayout = tab_business.getClass();
//        try {
//            Field tabStrip = tabLayout.getDeclaredField("mTabStrip");
//            tabStrip.setAccessible(true);
//
//            LinearLayout ll_tab = (LinearLayout) tabStrip.get(tab_business);
//            for (int i = 0; i < ll_tab.getChildCount(); i++) {
//                View child = ll_tab.getChildAt(i);
//                child.setPadding(0,0,0,0);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
//                params.setMarginStart(DensityUtils.dip2px(context,10f));
//                params.setMarginEnd(DensityUtils.dip2px(context,10f));
//                child.setLayoutParams(params);
//                child.invalidate();
//            }
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
}
