package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class DubboTestNg   extends AbstractApplicationTestNg {
    @Reference(version = "3.0.0")
    private ShortUrlService shortUrlService;

    @Reference(version = "3.0.0")
    private ShortUrlQueryRecordService shortUrlQueryRecordService;

    @Test
    public void testLongToShort() {
        String longUrl = "http://baidu.com";
        Long validate = 7L;

        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setLongUrl(longUrl);
        shortUrlDto.setValidDate(validate);

        ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
        System.out.println(shortUrlResult.getShortUrl());

        Assert.assertEquals(shortUrlResult.isSuccess(),true,"dubbo调用失败");
    }

    @Test
    public void testShortToLong() {
        String shortUrl = "fxSUH";
        //返回结果
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong(shortUrl);
        System.out.println(shortUrlResult.getLongUrl());
        //预期结果
        ShortUrlResult expected = new ShortUrlResult();
        expected.setLongUrl("http://baidu.com");
        expected.setShortUrl("shortUrlQueryRecordDto");
        expected.setResponseMsg("请求成功");
        expected.setSuccess(true);
        Assert.assertEquals(JSONObject.toJSONString(shortUrlResult),JSONObject.toJSONString(expected),"与预期结果不符");
    }


    @Test
    public void testGetErrorMsg() {
        ResponseBean responseBean = shortUrlQueryRecordService.getErrorMsg(0, 2);

    }

    @Test
    public void testCheckErrorMsg() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(1,true);

    }

}
