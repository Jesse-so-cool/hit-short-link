package cn.com.bluemoon.shorturl.servcie.impl;

import cn.com.bluemoon.shorturl.dto.PfShortUrlEntity;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.repository.PfShortUrlRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import cn.com.bluemoon.shorturl.util.DateUtils;
import cn.com.bluemoon.shorturl.util.RedisUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author XuZhuohao
 * @date 2019/9/18
 */
@Service(version = "2.0.0")
@org.springframework.stereotype.Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PfShortUrlRepository repository;

    @Override
    public ShortUrlResult longToShort(String longUrl, Long validTimeDay) {


        PfShortUrlEntity entity = new PfShortUrlEntity();
        entity.setIsValid((byte)1);
        entity.setLongUrl(longUrl);
        entity.setValidDate(DateUtils.newAddDay(validTimeDay));
        //TODO: 检查 create by 是否自动生成
        repository.save(entity);
        // 生成短链接

        // 保存到 redis

        return null;
    }

    @Override
    public ShortUrlResult shortToLong(String shortUrl) {
        return null;
    }
}
