package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class DubboTestNg   extends AbstractApplicationTestNg {
    @Reference(version = "3.0.0")
    private ShortUrlService shortUrlService;


    @Test
    public void testLongToShort() {
        String longUrl = "http://baidu.com";
        Long validate = 7L;

        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setLongUrl(longUrl);
        shortUrlDto.setValidDate(validate);

        ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
        System.out.println(shortUrlResult.getShortUrl());

        Assert.assertEquals(true,shortUrlResult.isSuccess(),"dubbo调用失败");
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
        expected.setShortUrl("fxSUH");
        expected.setResponseMsg("请求成功");
        expected.setSuccess(true);
        Assert.assertEquals(JSONObject.toJSONString(expected),JSONObject.toJSONString(shortUrlResult),"与预期结果不符");
    }

}
