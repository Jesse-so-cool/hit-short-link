package cn.com.jesse.shortlink.util;

/**
 * 时间工具
 *
 * @date 2019/9/19
 */
public class DateUtils {
    /**
     * 时间添加天数
     *
     * @param days 天数
     * @return timestamp
     */
    public static long addDay(Long date, Long days) {
        return date + days * 24 * 3600 * 1000;
    }

}
