package cn.lvsong.lib.net.cover

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

/** http://stackoverflow.com/questions/9483348/gson-treat-null-as-empty-string/24252578#24252578
 * Desc: 处理服务器返回 String = null 情况
 * Author: Jooyer
 * Date: 2018-11-11
 * Time: 22:03
 */
class StringNullAdapter : TypeAdapter<String>() {

    @Throws(IOException::class)
    override fun read(reader: JsonReader): String {
        // TODO Auto-generated method stub
        if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            return ""
        }
        return reader.nextString()
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: String?) {
        // TODO Auto-generated method stub
        if (value == null) {
            writer.nullValue()
            return
        }
        writer.value(value)
    }


}