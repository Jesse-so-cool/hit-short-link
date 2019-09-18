package cn.com.bluemoon.shorturl.servcie.impl;

import cn.com.bluemoon.shorturl.dto.ResultBean;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author XuZhuohao
 * @date 2019/9/18
 */

public class ShortUrlServiceImpl implements ShortUrlService {
    /**
     * redis key 前缀
     */
    private static final String URL_PREFIX = "middleware";

    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    public ResultBean longToShort(String longUrl) {
        return null;
    }

    @Override
    public ResultBean shortToLong(String shortUrl) {
        return null;
    }
}
