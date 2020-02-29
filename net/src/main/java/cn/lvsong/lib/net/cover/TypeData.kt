package cn.lvsong.lib.net.cover

import androidx.annotation.Keep

/** 注解: https://www.jianshu.com/p/d3721b171e9f / https://blog.csdn.net/feint123/article/details/77861740
 * Cover: https://blog.csdn.net/jdsjlzx/article/details/51860663
 * APT : https://blog.csdn.net/a296777513/article/details/79205798
 * Desc: 如果是列表 则 listKey="xxx" ,xxx 是后台返回的数据key,   如果返回的是 Json 对象, 则 dataKey="xxx"
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 18:31
 */
@Keep
@Target(AnnotationTarget.FUNCTION)
//@Retention(AnnotationRetention.RUNTIME) // 可以省略
annotation class TypeData(
        val successCode: Int = 200,
        val rspCodeKey: String = "code",
        val errorMsgKey: String = "msg",
        val dataKey: String = "", // data
        val listKey: String = "" // list
)
