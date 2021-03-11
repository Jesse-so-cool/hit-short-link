package cn.com.jesse.shortlink.servcie;

import cn.com.jesse.shortlink.dto.ShortUrlQueryRecordDto;
import com.bluemoon.pf.standard.bean.ResponseBean;

import java.util.List;

/**
 * @version v1.0
 * @ProjectName: short-url
 * @ClassName: ShortUrlQueryRecordService
 * @Author: mayuli
 * @Date: 2020/8/19 17:15
 */
public interface ShortUrlQueryRecordService {

    boolean save(List<ShortUrlQueryRecordDto> list);

    ResponseBean getErrorMsg(int start, int end);

    ResponseBean checkErrorMsg(int amount, boolean flag);

    void saveShortUrlQueryRecordDto(ShortUrlQueryRecordDto shortUrlQueryRecordDto);


}
