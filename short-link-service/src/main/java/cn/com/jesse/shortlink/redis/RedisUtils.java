package cn.com.jesse.shortlink.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
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
     * RedisTemplate无法使用middlewareSerializer、没找出原因，所以建议用StringRedisTemplate
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //添加Redis key前缀处理
        template.setKeySerializer(new MiddlewareSerializer());
        return template;
    }


    @Autowired
    private StringRedisTemplate template;

    /**
     * 保存到 redis
     *
     * @param key   key
     * @param value value
     * @param day   有效时间
     * @return
     */
    public boolean setData(String key, String value, Long day) {
        try {
            if (day == null) {
                template.opsForValue().set(key, value);
            } else {
                template.opsForValue().set(key, value, Duration.ofDays(day));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getData(String key) {
        return template.opsForValue().get(key);
    }

    public List<String> batchPopList(String key, int amount) {

        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setResultType(String.class);
        script.setScriptSource(new ResourceScriptSource(new
                ClassPathResource("redis.lua")));
        ArrayList keys = new ArrayList();
        keys.add(key);
        //key和args都会被keySerializer序列化 坑！
        Object res = template.execute(script, new StringRedisSerializer(), new Jackson2JsonRedisSerializer(List.class), keys, String.valueOf(0), String.valueOf(amount - 1));
        if (res == null) {
            return new ArrayList<>();
        }
        return (List<String>) res;
    }

    public boolean delete(String key) {
        return template.delete(key);
    }

    public void push(String key, String value) {
        template.opsForList().rightPush(key, value);

    }


    public List<String> range(String key, int start, int end) {

        return template.opsForList().range(key, start, end);

    }
}
