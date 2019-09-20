package cn.com.bluemoon.shorturl.servcie;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
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
     * @return 短链接
     */
    ShortUrlResult longToShort(ShortUrlDto shortUrlDto);

    ShortUrlResult shortToLong(String shortUrl);

}
