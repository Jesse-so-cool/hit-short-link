package cn.com.bluemoon.shorturl.servcie;

import cn.com.bluemoon.shorturl.dto.ShortUrlResult;

/**
 * 短链接服务
 *
 * @author XuZhuohao
 * @date 2019/9/18
 */
public interface ShortUrlService {
    /**
     * 长链接转短链接
     * @param longUrl 长链接
     * @param validTimeDay 有效期限（单位：天)
     * @return 短链接
     */
    ShortUrlResult longToShort(String longUrl, Long validTimeDay);

    ShortUrlResult shortToLong(String shortUrl);

}
