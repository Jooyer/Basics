package cn.lvsong.lib.library.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.lvsong.lib.library.R;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

/**
 * Desc: https://blog.csdn.net/smileiam/article/details/73603840
 * Author: Jooyer
 * Date: 2018-09-13
 * Time: 11:36
 */
public class StatusBarUtil {

    // SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN和SYSTEM_UI_FLAG_LAYOUT_STABLE，注意两个Flag必须要结合在一起使用，表示会让应用的主体内容占用系统状态栏的空间
    public static void transparentStatusBar(Activity activity,@ColorRes int statusBarColor, boolean hasCToAndFitSW) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);  //  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR   --> 文字颜色是黑色了
            } else {
                window.getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                if (RomUtil.isMiui()) {
                    mIUISetStatusBarLightMode(activity, false);
                } else if (RomUtil.isFlyme()) {
                    flyMeSetStatusBarLightMode(activity, false);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
            window.setStatusBarColor(ContextCompat.getColor(activity, statusBarColor));
            // 虚拟导航键
//            window.setNavigationBarColor(Color.YELLOW);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (hasCToAndFitSW) {
            contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        } else {
            contentView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 设置状态栏颜色
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置底部虚拟导航栏颜色
     */
    public static void setNavigationBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * @param dark --> true --> 状态栏文字黑色
     */
    public static void changeState(Activity activity, Boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 或者在 style  加入:  <item name="android:windowLightStatusBar">true</item>
            if (dark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        } else {
            if (RomUtil.isMiui()) {
                mIUISetStatusBarLightMode(activity, dark);
            } else if (RomUtil.isFlyme()) {
                flyMeSetStatusBarLightMode(activity, dark);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param activity
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean flyMeSetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        if (activity.getWindow() != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean mIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }


    // https://www.jianshu.com/p/205b8f5adb48
    public static void showStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void hideStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    // https://www.cnblogs.com/muhuacat/p/7447484.html
    public static void hideNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static void showNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 设置状态栏透明
     * https://blog.csdn.net/weixin_39947864/article/details/81906140 (详细 )
     * https://www.jb51.net/article/111936.htm
     *
     * @param activity
     * @param show
     */
    public static void setStatusBarVisible(Activity activity, boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags = uiFlags & 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    * View.SYSTEM_UI_FLAG_FULLSCREEN);
            uiFlags = uiFlags & 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    /**
     * 显示隐藏状态栏，全屏不变，只在有全屏时有效
     *
     * @param enable --> true 表示显示状态栏文字
     */
    public static void setStatusBarVisibility(Activity activity, boolean enable) {
        setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.main_theme_color));
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (enable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        activity.getWindow().setAttributes(lp);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    // https://www.jianshu.com/p/561f7241153b/
    // 当 SDK >= 9, 对刘海屏适配  (https://blog.csdn.net/DJY1992/article/details/80689632)
    public static void fitAndroidP(Activity activity) {
        /*
         1.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT  --> 仅仅当系统提供的bar完全包含了刘海区时才允许window扩展到刘海区，否则window不会和刘海区重叠
         2.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES  --> 允许window扩展到刘海区
         3.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER  --> 不允许window扩展到刘海区
        * */

//        WindowManager.LayoutParams windowManagerDu = activity.getWindow().getAttributes();
//        windowManagerDu.layoutInDisplayCutoutMode=WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        activity.getWindow().setAttributes(windowManagerDu );
    }

    // extraFlags
    /*
    0x00000100 开启配置
    0x00000200 竖屏配置
    0x00000400 横屏配置
    * */

    /**
     * 0x00000100 | 0x00000200 竖屏绘制到耳朵区
     * 0x00000100 | 0x00000400 横屏绘制到耳朵区
     * 0x00000100 | 0x00000200 | 0x00000400 横竖屏都绘制到耳朵区
     */
    // 可以隐藏刘海屏
    public static void fitXiaoMi(Activity activity) {


        try {
            Method method = Window.class.getMethod("addExtraFlags",
                    int.class);
            method.invoke(activity.getWindow(), 0x00000100 | 0x00000200);
        } catch (Exception e) {
            Log.i("StatusBar", "fitXiaoMi===========addExtraFlags not found.");
        }

    }

    // SystemProperties.getInt("ro.miui.notch", 0) == 1; 此时为刘海屏
    public static boolean fitXiaoMi(Activity activity, String key) {
        int result = 0;
        if (RomUtil.isMiui()) {
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = key;
                params[1] = 0;
                result = (Integer) getInt.invoke(SystemProperties, params);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result == 1;
    }


    public static boolean fitOppo(Activity activity) {
        // 返回 true为凹形屏 ，可识别OPPO的手机是否为凹形屏
        return activity.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }


    public static final int NOTCH_IN_SCREEN_VOIO_MARK = 0x00000020;//是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VOIO_MARK = 0x00000008;//是否有圆角

    public static boolean fitVivo(Activity activity, int mark) {
        boolean ret = false;
        try {
            ClassLoader cl = activity.getClassLoader();
            Class ftFeature = cl.loadClass("android.util.FtFeature");
            Method get = ftFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(ftFeature, mark);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        }
        return ret;
    }

    public static boolean fitHuaWei(Activity activity) {
        boolean ret = false;
        try {
            ClassLoader cl = activity.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        }
        return ret;
    }


}
