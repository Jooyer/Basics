package cn.lvsong.lib.library.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.DrawableRes;

import java.util.List;


/**
 * https://blog.csdn.net/woblog/article/details/51095087
 * <p>
 * Created by Jooyer on 2018/6/14
 * 分享
 */

public class ShareHelper {

    private static ShareHelper sShareHelper;

    private ShareHelper() {
    }

    public static ShareHelper getInstance() {
        if (null == sShareHelper) {
            synchronized (ShareHelper.class) {
                if (null == sShareHelper) {
                    sShareHelper = new ShareHelper();
                }
            }
        }
        return sShareHelper;
    }


    /*---------------------- 以下分析调用的是系统分享方法,只能分享文本/图片,不能同时分享文字和图片 -----------------------*/

    //分享到 QQ 好友
    public void shareToQQ(Activity context,String content,String appName) {
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName component = new ComponentName(
                "com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(component);
        intent.putExtra(Intent.EXTRA_TEXT,content);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent,appName));
    }


    // 分享图片到微信好友
    public void shareToWeiXin(Activity context, String appName, @DrawableRes int drawableId ) {
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName componentName = new ComponentName(
                "com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        Bitmap bt = BitmapFactory.decodeResource(context.getResources(), drawableId); // TODO

        final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bt, null, null));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, appName));
    }


    // 分享到朋友圈
    public void shareToWeiXinCircle(Activity context, @DrawableRes int drawableId, String appName) {
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName componentName = new ComponentName(
                "com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);
        // 下面方法把 asset 文件夹下文件拷贝到手机中,麻烦
//        System.out.println(new File("android_asset/app_qrcode.png")
//                .length());
//        File file = FileUtils
//                .getFile("share.png");
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            InputStream fis = context.getAssets().open(
//                    "app_qrcode.png");
//            byte[] buffer = new byte[2048];
//            int len = 0;
//            while ((len = fis.read(buffer)) != -1) {
//                fos.write(buffer, 0, len);
//            }
//            fis.close();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //将项目图片转换为uri
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(drawableId);
        Bitmap bt = bd.getBitmap();
        /*  手机图片
        File f = new File("图片路径");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
*/
        final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bt, null, null));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, appName));
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param packageName(包名) -->(若想判断QQ，则改为com.tencent.mobileqq，
     *                        若想判断微信，则改为com.tencent.mm)
     * @return true --> 包含
     */
    public boolean isAvailable(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pkInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pkInfo.size(); i++) {
            if (((PackageInfo) pkInfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

}
