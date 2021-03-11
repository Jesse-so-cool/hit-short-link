package cn.com.jesse.shortlink.servcie.impl;

import cn.com.jesse.shortlink.config.ShortUrlConfig;
import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.jesse.shortlink.dto.ShortUrlEntity;
import cn.com.jesse.shortlink.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.jesse.shortlink.interception.IpUtils;
import cn.com.jesse.shortlink.interception.UserAgentUtils;
import cn.com.jesse.shortlink.redis.RedisUtils;
import cn.com.jesse.shortlink.repository.ShortUrlRepository;
import cn.com.jesse.shortlink.servcie.ShortUrlQueryRecordService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import cn.com.jesse.shortlink.util.ConvertUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ShortUrlRepository repository;

    @Autowired
    private ShortUrlConfig shortUrlConfig;

    @Autowired
    private ShortUrlQueryRecordService shortUrlQueryRecordService;


    @Override
    public ShortUrlResult longToShort(ShortUrlDto shortUrlDto) {

        ShortUrlResult shortUrlResult = new ShortUrlResult(null, null, "请求失败", false);
        if (!checkParam(shortUrlDto, shortUrlResult)) {
            return shortUrlResult;
        }
        try {
            //加索引
            ShortUrlEntity urlEntity = repository.findByLongUrlAndAndIsValid(shortUrlDto.getLongUrl(), (byte) 1);
            if (urlEntity != null) {
                urlEntity.setCreateDate(new Timestamp(System.currentTimeMillis()));
                urlEntity.setValidDate(shortUrlDto.getValidDate() == null ? 100 * 365 : shortUrlDto.getValidDate());
                repository.save(urlEntity);
            } else {
                urlEntity = new ShortUrlEntity();
                urlEntity.setIsValid((byte) 1);
                urlEntity.setLongUrl(shortUrlDto.getLongUrl());
                urlEntity.setValidDate(shortUrlDto.getValidDate() == null ? 100 * 365 : shortUrlDto.getValidDate());
                repository.save(urlEntity);
            }
            // 生成短链接
            String base62 = ConvertUtil.toBase62(urlEntity.getId());
            long oneWeek = 7;
            if (shortUrlDto.getValidDate() == null || shortUrlDto.getValidDate() > oneWeek) {
                redisUtils.setData(base62, shortUrlDto.getLongUrl(), oneWeek);
            } else {
                redisUtils.setData(base62, shortUrlDto.getLongUrl(), shortUrlDto.getValidDate());
            }

            //最后一位加入随机数
            Random random = new Random();
            final int i = random.nextInt(61);
            shortUrlResult.setShortUrl(shortUrlConfig.getDomain() + base62 + ConvertUtil.BASE.charAt(i));

            shortUrlResult.setLongUrl(shortUrlDto.getLongUrl());
            shortUrlResult.setSuccess(true);
            shortUrlResult.setResponseMsg("请求成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        shortUrlResult.setLongUrl(shortUrlDto.getLongUrl());
        return shortUrlResult;
    }

    private boolean checkParam(ShortUrlDto shortUrlDto, ShortUrlResult shortUrlResult) {
        if (shortUrlDto == null || shortUrlDto.getLongUrl() == null || shortUrlDto.getLongUrl().length() > 255) {
            shortUrlResult.setResponseMsg("参数不规范");
            return false;
        }
        String regex = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        if (!Pattern.matches(regex, shortUrlDto.getLongUrl())) {
            shortUrlResult.setResponseMsg("url不规范");
            return false;
        }
        return true;
    }

    //@Transactional
    @Override
    public ShortUrlResult shortToLong(String shortUrl) {
        ShortUrlResult shortUrlResult = new ShortUrlResult(shortUrl, null, "请求失败", false);
        String ip = IpUtils.getIp();
        //切到最后一位 随机数
        shortUrl = shortUrl.substring(0, shortUrl.length() - 1);

        String longUrl = redisUtils.getData(shortUrl);
        ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
        shortUrlQueryRecordDto.setShortUrl(shortUrl);
        shortUrlQueryRecordDto.setIp(ip);
        shortUrlQueryRecordDto.setUserAgent(UserAgentUtils.getUserAgent());
        shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));

        if (StringUtil.isNullOrEmpty(longUrl)) {
            final long id = ConvertUtil.toBase10(shortUrl);
            final ShortUrlEntity op = repository.findByIdAndAndIsValid(id, (byte) 1);
            if (op != null) {
                final long now = System.currentTimeMillis();
                final ShortUrlEntity shortUrlEntity = op;
                if ((now - shortUrlEntity.getCreateDate().getTime()) < shortUrlEntity.getValidDate() * 3600 * 1000 * 24) {
                    //仍在有效期内,刷到redis
                    shortUrlQueryRecordDto.setLongUrl(shortUrlEntity.getLongUrl());
                    shortUrlQueryRecordService.saveShortUrlQueryRecordDto(shortUrlQueryRecordDto);
                    shortUrlResult.setLongUrl(shortUrlEntity.getLongUrl());
                } else {
                    //过期的短链 >>> 失败
                    shortUrlEntity.setIsValid((byte) 0);
                    repository.save(shortUrlEntity);
                    shortUrlResult.setLongUrl(shortUrlConfig.getDomain() + "expired.html");
                    shortUrlResult.setResponseMsg("短链已过期");
                    return shortUrlResult;
                }
            } else {
                //不存在的短链 >>> 失败
                shortUrlResult.setLongUrl(shortUrlConfig.getDomain() + "notExist.html");
                shortUrlResult.setResponseMsg("不存在的短链");
                return shortUrlResult;
            }
        } else {
            shortUrlQueryRecordDto.setLongUrl(longUrl);
            shortUrlQueryRecordService.saveShortUrlQueryRecordDto(shortUrlQueryRecordDto);
            shortUrlResult.setLongUrl(longUrl);
        }
        shortUrlResult.setResponseMsg("请求成功");
        shortUrlResult.setSuccess(true);
        return shortUrlResult;
    }

}
