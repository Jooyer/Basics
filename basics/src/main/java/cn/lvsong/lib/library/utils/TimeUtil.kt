package cn.lvsong.lib.library.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Desc: 时间工具
 * Author: Jooyer
 * Date: 2018-10-17
 * Time: 13:59
 */
object TimeUtil {

    /**
     *  获取当前月的天数
     */
    fun getDaysInCurrentMonth(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.set(Calendar.DATE, 1)
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE)
    }

    /**
     *  获取指定年和月 对应的天数
     */
    fun getDaysInYearAndMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DATE, 1)
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE)
    }

    /**
     *  获取当前是礼拜几
     *  返回值:
     *  1. pattern="E" 或者 pattern="EE"  返回 周x
     *  2. pattern="EEEE" 返回 星期x
     *  PS: MMMM代表中文月份，如“七月”；MM代表月份，如“07”；yyyy代表年份，如“2017”；dd代表天，如“05”
     */
    fun getWeekDayInCurrentMonth(): String {
        val date = Date()
        val format = SimpleDateFormat("EE", Locale.CHINA)
        return format.format(date)
    }

    /**
     *  获取当前是本周第几天
     */
    fun getRankDayInCurrentWeek(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.firstDayOfWeek = Calendar.MONDAY
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    /**
     *  获取当前是本年中第几周
     */
    fun getRankWeekInCurrentYear(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.timeInMillis = System.currentTimeMillis()
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }


    /**
     *  获取当前是本月第几天
     */
    fun getDayInCurrentMonth(): Int {
        // 注释部分效果一样,只不过返回时 String
//        val date = Date()
//        val format = SimpleDateFormat("dd", Locale.CHINA)
//        return format.format(date)
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     *  获取当前是本年第几天
     */
    fun getDayInCurrentYear(): String {
        val date = Date()
        val format = SimpleDateFormat("DD", Locale.CHINA)
        return format.format(date)
    }

    /**
     *  获取当前是本年第几月
     */
    fun getMonthInCurrentYear(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 生成当前时间后一天时间零点
     * 例如:当前时间为2016/7/18 15:44:30
     *           生成时间为2016/7/19 00:00:00
     */
    fun getNextDay() {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.time = Date()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        var date = Date()
        date = calendar.getTime()
        //将date类型的时间转换成String类型
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val str = sdf.format(date);
        System.out.println(str)
    }

    /**
     *  获取下一天零点时间
     */
    fun getNextDayUnicodeTime():Long{
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.set(Calendar.HOUR_OF_DAY, 0)  // Calendar.HOUR --> 一天中
        calendar.add(Calendar.DAY_OF_MONTH,1)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.MILLISECOND,0)
       return calendar.timeInMillis
    }


}