package cn.com.bluemoon.shorturl.servcie;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;

/**
 * 短链接服务
 */
public interface ShortUrlService {

    /**
     * 生成短链接
     * longUrl 长链接 必填
     * validDate 有效期(单位: 天)  非必填
     * @param shortUrlDto
     * @return
     */
    ShortUrlResult longToShort(ShortUrlDto shortUrlDto);

    /**
     * 短链接->长链接
     * @param uri 如http://bm.link/fxSNY中的fxSNY
     * @return
     */
    ShortUrlResult shortToLong(String uri);

}
