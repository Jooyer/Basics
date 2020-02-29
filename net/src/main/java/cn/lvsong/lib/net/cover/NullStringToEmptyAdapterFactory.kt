package cn.lvsong.lib.net.cover

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken



/**https://blog.csdn.net/anskya520/article/details/52856428 --> 利用catch
 * Desc: 处理服务器返回 String = null 情况
 * Author: Jooyer
 * Date: 2018-11-11
 * Time: 22:01
 */
class NullStringToEmptyAdapterFactory : TypeAdapterFactory {
    override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType as Class<T>
        return if (rawType != String::class.java) {
            null
        } else StringNullAdapter() as TypeAdapter<T>
    }
}