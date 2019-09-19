package cn.com.bluemoon.shorturl.util;

import java.sql.Timestamp;

/**
 * 时间工具
 *
 * @author XuZhuohao
 * @date 2019/9/19
 */
public class DateUtils {
    /**
     * 当前时间添加天数
     * @param days 天数
     * @return timestamp
     */
    public static Timestamp newAddDay(Long days){
        return new Timestamp(System.currentTimeMillis() + days * 24 * 3600 * 1000);
    }
}
