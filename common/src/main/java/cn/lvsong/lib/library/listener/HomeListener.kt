package cn.lvsong.lib.library.listener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

/**
 * Desc: 监听手机 clickHome 键  和 菜单键(最近任务键)
 * Author: Jooyer
 * Date: 2018-11-16
 * Time: 17:17
 */
class HomeListener(var mContext: Context?) {
    private var mOnKeyClickListener: OnKeyClickListener? = null
    private var mHomeBtnIntentFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    private var mHomeBtnReceiver = HomeBtnReceiver()

    inner class HomeBtnReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val reason = intent.getStringExtra("reason")
                if (reason != null) {
                    if (null != mOnKeyClickListener) {
                        when (reason) {
                            "homekey" -> //按 Home 按键
                                mOnKeyClickListener!!.clickHome()
                            "recentapps" -> //最近任务键也就是菜单键
                                mOnKeyClickListener!!.clickRecent()
                            "assist" -> //常按 home 键盘
                                mOnKeyClickListener!!.longClickHome()
                        }
                    }
                }
            }
        }
    }

    interface OnKeyClickListener {
        /**
         * 点击 Home 键
         */
        fun clickHome()
        /**
         * 长按 Home 键
         */
        fun longClickHome()
        /**
         * 点击最近任务栏键
         */
        fun clickRecent()
    }

    companion object {
        private val TAG = "HomeListener"
    }


    /**
     * 开始监听手机三大按钮
     */
    fun startListen() {
        if (mContext != null)
            mContext!!.registerReceiver(mHomeBtnReceiver, mHomeBtnIntentFilter)
        else
            Log.e(TAG, "mContext is null and startListen fail")

    }

    /**
     * 停止监听手机三大按钮
     */
    fun stopListen() {
        if (mContext != null)
            mContext!!.unregisterReceiver(mHomeBtnReceiver)
        else
            Log.e(TAG, "mContext is null and stopListen fail")
    }

    fun setOnKeyClickListener(onKeyClickListener: OnKeyClickListener) {
        mOnKeyClickListener = onKeyClickListener

    }


}
