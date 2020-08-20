package cn.com.bluemoon.shorturl.servcie;

import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;

import java.util.List;

/**
 * @version v1.0
 * @ProjectName: short-url
 * @ClassName: ShortUrlQueryRecordService
 * @Description: TODO(一句话描述该类的功能)
 * @Author: mayuli
 * @Date: 2020/8/19 17:15
 */
public  interface ShortUrlQueryRecordService {
      void save(List<ShortUrlQueryRecordDto> list);
}
