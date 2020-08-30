package cn.lvsong.lib.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/** https://blog.csdn.net/NJP_NJP/article/details/103524778
 * Desc: 对 ViewModel 扩展
 * Author: Jooyer
 * Date: 2020-08-30
 * Time: 9:20
 */

fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (e: Throwable) -> Unit = {},
    onComplete: () -> Unit = {}

) {
    viewModelScope.launch(CoroutineExceptionHandler{_,e -> onError(e)}){
       try {
           block.invoke(this)
       }finally {
           onComplete()
       }
    }
}
