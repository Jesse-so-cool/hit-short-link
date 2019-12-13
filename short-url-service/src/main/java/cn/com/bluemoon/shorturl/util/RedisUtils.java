package cn.com.bluemoon.shorturl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * redis 操作
 *
 * @date 2019/9/19
 */
@Component
public class RedisUtils {
    /**
     * redis key 前缀
     */
    private static final String URL_PREFIX = "middleware_";

    @Autowired
    private RedisTemplate<String, String> template;

    /**
     * 保存到 redis
     * @param key key
     * @param value value
     * @param day 有效时间
     * @return
     */
    public boolean setData(String key, String value, Long day){
        try {
            if(day == null) {
                template.opsForValue().set(URL_PREFIX + key, value);
            } else{
                template.opsForValue().set(URL_PREFIX + key, value, Duration.ofDays(day));
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public String getData(String key){
        return template.opsForValue().get(URL_PREFIX + key);
    }

    public void delete(String key){
        template.delete(URL_PREFIX + key);
    }

}
