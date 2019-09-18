package cn.com.bluemoon.shorturl.servcie.impl;

import cn.com.bluemoon.shorturl.dto.ResultBean;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.repository.PfShortUrlRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author XuZhuohao
 * @date 2019/9/18
 */
@Service(version = "2.0.0")
public class ShortUrlServiceImpl implements ShortUrlService {
    /**
     * redis key 前缀
     */
    private static final String URL_PREFIX = "middleware_";

    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private PfShortUrlRepository repository;

    @Override
    public ShortUrlResult longToShort(String longUrl, Long validTimeDay) {
        return null;
    }

    @Override
    public ShortUrlResult shortToLong(String shortUrl) {
        return null;
    }
}
