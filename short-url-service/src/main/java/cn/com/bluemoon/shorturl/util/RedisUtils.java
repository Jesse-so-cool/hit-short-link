package cn.com.bluemoon.shorturl.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * redis 操作
 *
 * @date 2019/9/19
 */
@Slf4j
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

    public void push(String key, String value){
        template.opsForList().rightPush(URL_PREFIX + key, value);
    }

    public List<String> batchPopList(String key, int amount){
        List<String> range = new ArrayList<>();
        try {
            ListOperations<String, String> opsForList = template.opsForList();
            range = opsForList.range(URL_PREFIX + key, 0, amount - 1);
            opsForList.trim(URL_PREFIX+key,range.size() - 1, -1);
        }catch (Throwable throwable){
            log.error(throwable.getLocalizedMessage());
            return new ArrayList<>();
        }

        return range;
    }
}
