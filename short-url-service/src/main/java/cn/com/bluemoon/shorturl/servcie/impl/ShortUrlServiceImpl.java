package cn.com.bluemoon.shorturl.servcie.impl;

import cn.com.bluemoon.shorturl.config.ShortUrlConfig;
import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlEntity;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.repository.ShortUrlRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import cn.com.bluemoon.shorturl.util.ConvertUtil;
import cn.com.bluemoon.shorturl.util.RedisUtils;
import com.alibaba.dubbo.config.annotation.Service;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author XuZhuohao
 * @date 2019/9/18
 */
@Service(version = "${version.short-url.service}")
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ShortUrlRepository repository;

    @Autowired
    private ShortUrlConfig shortUrlConfig;
    @Override
    public ShortUrlResult longToShort(ShortUrlDto shortUrlDto) {

        ShortUrlResult shortUrlResult = new ShortUrlResult(null,null,"请求失败",false);
        if (!checkParam(shortUrlDto,shortUrlResult)) {
            return shortUrlResult;
        }
        try {
            ShortUrlEntity urlEntity = repository.findByLongUrlAndAndIsValid(shortUrlDto.getLongUrl(), (byte) 1);
            if (urlEntity!=null) {
                urlEntity.setCreateDate(new Timestamp(System.currentTimeMillis()));
                urlEntity.setValidDate(shortUrlDto.getValidDate()==null ? 100*365:shortUrlDto.getValidDate());
                repository.save(urlEntity);
            }else {
                urlEntity = new ShortUrlEntity();
                urlEntity.setIsValid((byte)1);
                urlEntity.setLongUrl(shortUrlDto.getLongUrl());
                urlEntity.setValidDate(shortUrlDto.getValidDate()==null ? 100*365:shortUrlDto.getValidDate());
                repository.save(urlEntity);
            }
            // 生成短链接
            String base62 = ConvertUtil.toBase62(urlEntity.getId());
            // 保存到 redis
            redisUtils.setData(base62,shortUrlDto.getLongUrl(),shortUrlDto.getValidDate());

            shortUrlResult.setShortUrl(shortUrlConfig.getDomain()+base62);
            shortUrlResult.setLongUrl(shortUrlDto.getLongUrl());
            shortUrlResult.setSuccess(true);
            shortUrlResult.setResponseMsg("请求成功");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return shortUrlResult;
    }

    private boolean checkParam(ShortUrlDto shortUrlDto, ShortUrlResult shortUrlResult) {
        if (shortUrlDto==null || shortUrlDto.getLongUrl()==null || shortUrlDto.getLongUrl().length()>255) {
            shortUrlResult.setResponseMsg("参数不规范");
            return false;
        }
        if (shortUrlDto.getLongUrl().indexOf("http://")<0 && shortUrlDto.getLongUrl().indexOf("https://")<0) {
            shortUrlDto.setLongUrl("http://"+shortUrlDto.getLongUrl());
        }

        return true;
    }

    @Override
    public ShortUrlResult shortToLong(String shortUrl) {
        ShortUrlResult shortUrlResult = new ShortUrlResult(shortUrl,null,"请求失败",false);

        String longUrl = redisUtils.getData(shortUrl);
        if (StringUtil.isNullOrEmpty(longUrl)) {
            final long id = ConvertUtil.toBase10(shortUrl);
            final Optional<ShortUrlEntity> op = repository.findById(id);
            if (op.isPresent()) {
                final long now = System.currentTimeMillis();
                final ShortUrlEntity shortUrlEntity = op.get();
                if ((now-shortUrlEntity.getCreateDate().getTime()) < shortUrlEntity.getValidDate()*3600*1000*24) {
                    //Duration.between();
                    redisUtils.setData(ConvertUtil.toBase62(shortUrlEntity.getId()),shortUrlEntity.getLongUrl(),shortUrlEntity.getValidDate());

                    shortUrlResult.setLongUrl(shortUrlEntity.getLongUrl());
                    shortUrlResult.setResponseMsg("请求成功");
                    shortUrlResult.setSuccess(true);
                }else {
                    shortUrlEntity.setIsValid((byte) 0);
                    repository.save(shortUrlEntity);

                    shortUrlResult.setLongUrl("expired.html");
                    shortUrlResult.setSuccess(true);
                }
            }
        }else {
            shortUrlResult.setLongUrl(longUrl);
            shortUrlResult.setResponseMsg("请求成功");
            shortUrlResult.setSuccess(true);
        }
        return shortUrlResult;
    }
}
