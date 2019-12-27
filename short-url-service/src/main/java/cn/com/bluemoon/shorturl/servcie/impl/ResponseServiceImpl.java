package cn.com.bluemoon.shorturl.servcie.impl;


import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.servcie.ResponseService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.bluemoon.pf.mgr.anno.BmAnno;
import com.bluemoon.pf.mgr.anno.BmBizAction;
import com.bluemoon.pf.mgr.anno.BmParam;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.standard.constants.ResponseCodeEnum;
import com.bluemoon.pf.standard.utils.ResponseBeanUtil;

import javax.xml.ws.Response;

@Service(version = "3.0.0")
public class ResponseServiceImpl implements ResponseService{
    @Reference(version = "${version.short-url.service}")
    private ShortUrlService shortUrlService;

    @Override
    public ResponseBean longToShort(@BmParam ShortUrlDto shortUrlDto) {
        try{
            ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
            ResponseBean scBean = ResponseBeanUtil.createScBean(shortUrlResult);
            return scBean;
        }catch (Exception e){
            e.printStackTrace();
            ResponseBean failBean = ResponseBeanUtil.createFailBean(ResponseCodeEnum.FAIL.getCode(), e.getMessage());
            return failBean;
        }
    }

    @Override
    public ResponseBean shortToLong(@BmParam String uri) {
        try{
            ShortUrlResult shortUrlResult = shortUrlService.shortToLong(uri);
            ResponseBean scBean = ResponseBeanUtil.createScBean(shortUrlResult);
            return scBean;
        }catch (Exception e){
            e.printStackTrace();
            ResponseBean failBean = ResponseBeanUtil.createFailBean(ResponseCodeEnum.FAIL.getCode(), e.getMessage());
            return failBean;
        }
    }
}
