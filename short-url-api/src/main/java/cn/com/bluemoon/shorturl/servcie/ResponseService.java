package cn.com.bluemoon.shorturl.servcie;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import com.bluemoon.pf.standard.bean.ResponseBean;

public interface ResponseService {
    ResponseBean longToShort(ShortUrlDto shortUrlDto);
    ResponseBean shortToLong(String uri);
}
