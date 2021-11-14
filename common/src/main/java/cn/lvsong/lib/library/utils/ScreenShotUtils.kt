package cn.lvsong.lib.library.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.utils.FileUtil
import cn.lvsong.lib.library.utils.StatusBarUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/** https://www.cnblogs.com/Free-Thinker/p/6394723.html  --> 图片压缩
 * Desc: 截屏
 * Author: Jooyer
 * Date: 2018-09-27
 * Time: 17:48
 */
object ScreenShotUtils {

    /**
     *
     * @param hasNotch  --> 是否有刘海...
     */
    fun screenShot(view: View, path: String): Boolean {
        val file = FileUtil.createFile(path)
        file.deleteOnExit()
        val realWidth = view.width
        val realHeight = view.height
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()

        val bp = Bitmap.createBitmap(
            view.getDrawingCache(true),
            0, 0, realWidth, realHeight
        )
        // 以清空画图缓冲区，否则下一次还是原来的图像
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        try {
            if (null != bp) {
                val fos = FileOutputStream(file)
                bp.compress(Bitmap.CompressFormat.PNG, 40, fos)
                fos.flush()
                fos.close()
                // 压缩图片
                compressImage(view.context, file, realWidth, realHeight)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 对 decorView 截屏
     */
    fun screenShot(activity: Activity): Boolean {
        val file = FileUtil.newFile(FileUtil.SCREEN_SHOT_NAME)
        val decorView = activity.window.decorView
        val statusBarHeight = StatusBarUtil.getStatusBarHeight(activity)
        val realWidth = DensityUtil.getRealWidth(activity)
        // ScreenUtils.getRealHeight() 只是减去底部导航栏的高度...
        val realHeight = DensityUtil.getScreenHeight(activity) - statusBarHeight
//        println("screenShot==========realWidth: $realWidth  --> realHeight: $realHeight  --> statusBarHeight: $statusBarHeight")
        decorView.isDrawingCacheEnabled = true
        decorView.buildDrawingCache()

        val bp = Bitmap.createBitmap(
            decorView.getDrawingCache(true),
            0, 0, realWidth, realHeight
        )
        decorView.isDrawingCacheEnabled = false
        decorView.destroyDrawingCache()
        try {
            if (null != bp) {
                val fos = FileOutputStream(file)
                bp.compress(Bitmap.CompressFormat.PNG, 40, fos)
                fos.flush()
                fos.close()

                compressImage(activity, file, realWidth, realHeight)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }


    private fun compressImage(context: Context, file: File, displayWidth: Int, displayHeight: Int) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//只测量image 不加载到内存
        BitmapFactory.decodeFile(file.absolutePath, options)//测量image
        options.inPreferredConfig = Bitmap.Config.RGB_565//设置565编码格式 省内存
        options.inSampleSize = calculateInSampleSize(options, displayWidth, displayHeight)//获取压缩比 根据当前屏幕宽高去压缩图片
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)//按照Options配置去加载图片到内存
        val out = ByteArrayOutputStream()//字节流输出
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)//压缩成JPEG格式 压缩像素质量为50%
        try {
            // 保存到相册
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentResolver = context.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                //图片需要多传一个参数，声明图片的类型，
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                contentValues.put(MediaStore.Images.Media.TITLE, file.name)
                // 图片生成时间
                contentValues.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
                contentValues.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis() / 1000)
                // 插入外置卡路径
                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (null != uri && null != bitmap) {
                    try {
                        val outputStream = contentResolver.openOutputStream(uri)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else {
                val dstFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), file.name)
                val fos = FileOutputStream(dstFile)//创建一个文件输出流
                val bytes = out.toByteArray()//字节数组
                val count = bytes.size//字节数组的长度
                fos.write(bytes, 0, count)//写到文件中
                fos.close()//关闭流
                out.close()

                // 更新相册
                MediaScannerConnection.scanFile(context, arrayOf(dstFile.absolutePath), arrayOf("image/jpeg")) { path: String, uri: Uri ->
                    android.util.Log.e("CopyBook", "scanFile=====>>>> ")
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {//计算图片的压缩比
        val height = options.outHeight//图片的高度
        val width = options.outWidth//图片的宽度 单位1px 即像素点

        var inSampleSize = 1//压缩比

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

/*  夜间模式切换，需要一个动画效果

		public File shot(){
		if (activity == null) {
			return;
		}
		final ViewGroup container = (ViewGroup) activity.getWindow().getDecorView();
		if (container == null) {
			return;
		}
		final ImageView imageView = new ImageView(activity);
		container.setDrawingCacheEnabled(true);
		Bitmap drawingCache = null;
		try {
			Bitmap cache = container.getDrawingCache();
			if (cache != null && !cache.isRecycled()) {
				drawingCache = Bitmap.createBitmap(cache);
			} else {
				Log.e(TAG, "showChangeNightModeAnim : container.getDrawingCache() = " + cache);
			}
		} catch (OutOfMemoryError error) {
			Log.e(TAG, "showChangeNightModeAnim : createBitmap OOM!!!");
			drawingCache = null;
		}
		if (drawingCache != null && !drawingCache.isRecycled()) {
			imageView.setImageBitmap(drawingCache);
		}
		container.destroyDrawingCache();
		container.setDrawingCacheEnabled(false);// 以清空画图缓冲区，否则下一次还是原来的图像
		imageView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		container.addView(imageView);
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "alpha", imageView.getAlpha(), 0f);
		objectAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				imageView.setImageBitmap(null);
				container.removeView(imageView);
			}
		});
		if (animatorListener != null) {
			objectAnimator.addListener(animatorListener);
		}
		objectAnimator.setDuration(Constant.CHANGE_NIGHT_ANIM_DURATION);
		objectAnimator.setInterpolator(new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return input * input * input;
			}
		});
		objectAnimator.start();
	}

	 */
}