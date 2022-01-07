package cn.lvsong.lib.library.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object TimeUtil {
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private val fullFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val yearMonthAndDayFormat = SimpleDateFormat("yyyy-MM-dd")
    private val yearAndMonthFormat = SimpleDateFormat("yyyy-MM")

    /**
     * 获取本月多少天
     */
    fun getDaysInCurrentMonth(): Int {
        val calender = Calendar.getInstance(Locale.CHINA)
        calender.set(Calendar.DATE, 1)
        calender.roll(Calendar.DATE, -1)
        return calender.get(Calendar.DATE)
    }

    /**
     * 获取指定年月的某月天数
     */
    fun getDaysInYearAndMonth(year: Int, month: Int): Int {
        val calender = Calendar.getInstance(Locale.CHINA)
        calender.set(Calendar.YEAR, year)
        calender.set(Calendar.MONTH, month - 1)
        calender.set(Calendar.DATE, 1)
        calender.roll(Calendar.DATE, -1)
        return calender.get(Calendar.DATE)
    }

    /**
     * 获取某天是本月礼拜几
     */
    fun getWeekDayInCurrentMonth(): String {
        val date = Date()
        val format = SimpleDateFormat("EE")
        return format.format(date)
    }

    /**
     * 本周是本年度第几周
     */
    fun getRankWeekInCurrentYear(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }


    /**
     * 获取某天是本周第几天,注意星期天为第一天
     */
    fun getRankDayInCurrentWeek(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    /**
     * 当前是本月第几天
     */
    fun getRankDayInCurrentMonth(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 当前在本年度中第几天
     */
    fun getRankDayInCurrentYeah(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 当前是本年第几月
     */
    fun getMonthInCurrentYeah(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 把时间转为  yyyy-MM-dd HH:mm,默认转换当前时间
     */
    fun getFullTimeByFormat(date: Date = Date()): String {
        return fullFormat.format(date)
    }

    /**
     * 把时间转为  yyyy-MM-dd HH:mm:ss,默认转换当前时间
     */
    fun getFullTimeByFormat2(date: Date = Date()): String {
        return fullFormat2.format(date)
    }

    /**
     * 把时间转为  yyyy-MM,默认转换当前时间
     */
    fun getYearAndMonthByFormat(date: Date = Date()): String {
        return yearAndMonthFormat.format(date)
    }

    /**
     * 把时间转为  yyyy-MM-dd,默认转换当前时间
     */
    fun getYearMonthAndDayByFormat(date: Date = Date()): String {
        return yearMonthAndDayFormat.format(date)
    }

    /**
     * 根据输入的日期(eg: 2020-05-28 11:02:55 )获取10位时间戳
     * @param time ->时间格式是这样的 yyyy-MM-dd HH:mm:ss
     * return 10位时间戳
     */
    fun getLongByStr(time: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return format.parse(time).time / 1000
    }

    /**
     * 根据输入的日期和日期格式获取10位时间戳,输入的时间越精确,返回的时间戳也越精确
     * @param time -> 日期
     * @param pattern -> 输入的日期格式
     * PS: 输入的日期和pattern必须一致
     */
    fun getLongByStrAndPattern(time: String, pattern: String): Long {
        val format = SimpleDateFormat(pattern, Locale.CHINA)
        return format.parse(time).time / 1000
    }

    /**
     * 根据输入的时间戳和时间格式转为相应的日期
     * @param time -> 10位时间戳
     * @param pattern -> 转换后的格式
     */
    fun getFormatTimeByTimestamp(time: String, pattern: String): String {
        val format = SimpleDateFormat(pattern, Locale.CHINA)
        return format.format(Date(time.toLong() * 1000))
    }

    /**
     * 根据时间戳获取其为 周几
     * @param time -> 10位时间戳
     */
    fun getWeekDayByTimestamp(time: String): String {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.time = Date(time.toLong() * 1000)
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            1 -> {
                "周日"
            }
            2 -> {
                "周一"
            }
            3 -> {
                "周二"
            }
            4 -> {
                "周三"
            }
            5 -> {
                "周四"
            }
            6 -> {
                "周五"
            }
            else -> {
                "周六"
            }
        }
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