package cn.lvsong.lib.library.utils

import android.content.Context

/** https://blog.csdn.net/ouyang_peng/article/details/53328000
 * Desc: 工具类，可以通过资源名来获取资源id
 * Author: Jooyer
 * Date: 2018-12-10
 * Time: 14:03
 */
object ResourceUtil {

    /**
     * @param resourceName --> 资源名不要后缀,如 同桌的你.mp3, 取 同桌的你 即可
     */
    fun getId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "id")
    }

    fun getLayoutId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "layout")
    }

    fun getStringId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "string")
    }

    fun getDrawableId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "drawable")
    }

    fun getMipmapId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "mipmap")
    }

    fun getRawId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "raw")
    }

    fun getColorId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "color")
    }

    fun getDimenId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "dimen")
    }

//    fun getAttrId(context: Context, resourceName: String): Int {
//        return getIdentifierByType(context, resourceName, "attr")
//    }

    fun getStyleId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "style")
    }

    fun getAnimId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "anim")
    }

    fun getArrayId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "array")
    }

    fun getIntegerId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "integer")
    }

    fun getBoolId(context: Context, resourceName: String): Int {
        return getIdentifierByType(context, resourceName, "bool")
    }

    private fun getIdentifierByType(context: Context, resourceName: String, defType: String): Int {
        return context.resources.getIdentifier(resourceName,
                defType,
                context.packageName)
    }

}
