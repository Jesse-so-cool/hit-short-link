package cn.com.bluemoon.shorturl.redis;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 自定义序列化器 统一前缀
 * @author jesse hsj
 * @date 2020/8/18 14:18
 */
public class MiddlewareSerializer extends StringRedisSerializer {

    /**
     * redis key 前缀
     */
    private static final String URL_PREFIX = "short_url_";
    private static final int length = URL_PREFIX.length();

    @Override
    public String deserialize(byte[] bytes) {
        String deserialize = super.deserialize(bytes);
        if (deserialize == null  || deserialize.length() == 0 || !deserialize.startsWith(URL_PREFIX)) {
            throw new RuntimeException("deserialize fail");
        }
        return deserialize.substring(length);
    }

    @Override
    public byte[] serialize(String string) {
        if (string == null || string.length() == 0){
            throw new RuntimeException("serialize fail");
        }
        return super.serialize(URL_PREFIX + string);
    }


}
