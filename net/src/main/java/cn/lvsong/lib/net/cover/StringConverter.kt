package cn.lvsong.lib.net.cover

import com.google.gson.*
import java.lang.reflect.Type

/** https://segmentfault.com/q/1010000002748900
 * Desc: 关于Gson解析json数据时如果属性值为null则会报空的问题
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 12:01
 */
@Deprecated("暂时无用")
class StringConverter : JsonSerializer<String>, JsonDeserializer<String> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): String {
        return if (json.asJsonPrimitive.asString.isNullOrEmpty()) "" else json.asJsonPrimitive.asString
    }

    override fun serialize(src: String?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (src.isNullOrEmpty()) JsonPrimitive("") else JsonPrimitive(src)
    }


}