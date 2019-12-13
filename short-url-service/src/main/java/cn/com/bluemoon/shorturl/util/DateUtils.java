package cn.com.bluemoon.shorturl.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 时间工具
 *
 * @date 2019/9/19
 */
public class DateUtils {
    /**
     * 时间添加天数
     * @param days 天数
     * @return timestamp
     */
    public static long addDay(Long date, Long days){
        return date + days * 24 * 3600 * 1000;
    }

}
