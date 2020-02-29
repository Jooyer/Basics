package cn.lvsong.lib.net.cover

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter

/** https://blog.csdn.net/calm1516/article/details/83414968  --> 自定义 TypeAdapter,也可以处理返回字段异常的
 * Desc: 对服务器返回的数据进行操作
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 10:25
 */
class DataResponseBodyConverter<T>(
    private val adapter: TypeAdapter<T>, private val gson: Gson,
    private val annotation: TypeData
) : Converter<ResponseBody, T> {
    private val TAG = DataResponseBodyConverter::class.java.simpleName

    override fun convert(value: ResponseBody): T {
        // @FIXME 可以约定,如果加密则返回字符串,反之返回 json

        // 如果返回数据有加密,需要用这个
//            val bytes = value.bytes()
//        Log.e(TAG,"convert========= ${String(bytes)}")
//            //对字节数组进行解密操作
//            val decryptString = RSAUtil.decryptByPublicKeyForSpilt(bytes,)
//            //对解密的字符串进行处理
//            val position = decryptString.lastIndexOf("}")
//            val jsonString = decryptString.substring(0, position + 1)
//
//            Log.i(TAG, "需要解密的服务器数据字节数组：" + bytes.toString())
//            Log.i(TAG, "解密后的服务器数据字符串：" + decryptString)
//            Log.i(TAG, "解密后的服务器数据字符串处理为json：" + jsonString)


        val rspStr = value.string()
        val jsonObj = gson.fromJson(rspStr, JsonObject::class.java)
            ?: throw IllegalArgumentException("Json 数据异常")
        val rspCode = jsonObj.getAsJsonPrimitive(annotation.rspCodeKey).asInt
        val msg = jsonObj.getAsJsonPrimitive(annotation.errorMsgKey).asString

        val json = JsonObject()
        json.addProperty(annotation.errorMsgKey, msg)
        json.addProperty(annotation.rspCodeKey, rspCode)

        if (annotation.successCode != rspCode) {
            throw ApiException(rspCode, msg)
        }

        return when {
            annotation.listKey.isNotEmpty() -> {
                adapter.fromJsonTree(jsonObj.get(annotation.listKey)) ?: adapter.fromJsonTree(json)
            }
            annotation.dataKey.isNotEmpty() -> {
                adapter.fromJsonTree(jsonObj.get(annotation.dataKey)) ?: adapter.fromJsonTree(json)
            }
            else -> {
                adapter.fromJsonTree(json)
            }
        }
    }


    /*

    package cn.lvsong.lib.net.cover

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * Desc: 对服务器返回的数据进行操作
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 10:25
 */
class DataResponseBodyConverter<T>(private val adapter: TypeAdapter<T>, private val gson: Gson,
                                   private val annotation: TypeData
) : Converter<ResponseBody, T> {
    private val TAG = DataResponseBodyConverter::class.java.simpleName

    override fun convert(value: ResponseBody): T {
        try {

            // @FIXME 可以约定,如果加密则返回字符串,反之返回 json


            // 如果返回数据有加密,需要用这个
//            val bytes = value.bytes()
//            //对字节数组进行解密操作
//            val decryptString = RSAUtil.decryptByPublicKeyForSpilt(bytes,)
//            //对解密的字符串进行处理
//            val position = decryptString.lastIndexOf("}")
//            val jsonString = decryptString.substring(0, position + 1)
//
//            Log.i(TAG, "需要解密的服务器数据字节数组：" + bytes.toString())
//            Log.i(TAG, "解密后的服务器数据字符串：" + decryptString)
//            Log.i(TAG, "解密后的服务器数据字符串处理为json：" + jsonString)


            val rspStr = value.string()
            val jsonObj = gson.fromJson(rspStr, JsonObject::class.java)
                    ?: throw IllegalArgumentException("Json 数据异常")
//            val json = JsonObject()
            val rspCode = jsonObj.getAsJsonPrimitive(annotation.rspCodeKey).asInt
            val msg = jsonObj.getAsJsonPrimitive(annotation.errorMsgKey).asString

            if (annotation.successCode != rspCode) {
//                json.addProperty(annotation.rspCodeKey,rspCode)
                throw ApiException(rspCode,msg)
//                when (rspCode) { // 根据不同错误码,返回不同错误提示
//                    404 -> {
//                        json.addProperty(annotation.errorMsgKey,"地址不正确...")
//                        return adapter.fromJsonTree(json)
//                    }
//                    504 -> {
//                        json.addProperty(annotation.errorMsgKey,"本地没有缓存...")
//                        return adapter.fromJsonTree(json)
//                    }
//                    603 -> {
//                        json.addProperty(annotation.errorMsgKey,"登录过期...")
//                        return adapter.fromJsonTree(json)
//                    }
//                    else -> {
//                        json.addProperty(annotation.errorMsgKey,msg)
//                        return adapter.fromJsonTree(json)
//                    }
//                }
            }

            var jsonTree: T? = null
            when {
                annotation.listKey.isNotEmpty() -> {
                    val element = jsonObj.get(annotation.listKey)
                    when (element) {
                        is JsonArray -> jsonTree = adapter.fromJsonTree(element)
                        is JsonObject -> jsonTree = adapter.fromJsonTree(element)
                    }
                    if (null == jsonTree){
                        return adapter.fromJsonTree(JsonArray())
                    }
                }
                annotation.dataKey.isNotEmpty() -> {
                    jsonTree = adapter.fromJsonTree(jsonObj.get(annotation.dataKey))
                    if (null == jsonTree){
                        return adapter.fromJsonTree(JsonObject())
                    }
                }
                else -> {
                    jsonTree = adapter.fromJsonTree(jsonObj)
                }
            }
            if (null != jsonTree) {
                return jsonTree
            } else {
                return adapter.fromJsonTree(JsonObject())
            }
        } catch (e: Exception) {
            println("convert================Exception ${e.message}")
            throw IllegalArgumentException("非法参数异常!")
        } finally {
            value.close()
        }

    }
}

     */
}