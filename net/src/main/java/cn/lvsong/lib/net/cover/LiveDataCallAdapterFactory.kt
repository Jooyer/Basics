package cn.lvsong.lib.net.cover

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *  LiveData + Retrofit
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    /**
     *  判断是不是 LiveData
     */
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (returnType is ParameterizedType) {
            throw IllegalArgumentException("返回值需为参数化类型")
        }

        // 获取 returnType 的 class 类型
        val returnClass = CallAdapter.Factory.getRawType(returnType)
        if (LiveData::class.java != returnClass){
            throw IllegalArgumentException("返回值不是 LiveData 类型")
        }

        val type = CallAdapter.Factory.getParameterUpperBound(0, returnType as ParameterizedType)
        return LiveDataAdapter<Any>(type)

    }




}