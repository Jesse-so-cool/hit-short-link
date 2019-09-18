package cn.com.bluemoon.shorturl.servcie;

import cn.com.bluemoon.shorturl.dto.ResultBean;

/**
 * 短链接服务
 *
 * @author XuZhuohao
 * @date 2019/9/18
 */
public interface ShortUrlService {

    ResultBean longToShort(String longUrl);

    ResultBean shortToLong(String shortUrl);

}
