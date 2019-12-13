package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class DubboTestNg   extends AbstractApplicationTestNg {
    @Reference(version = "3.0.0")
    private ShortUrlService shortUrlService;


    @Test
    public void testLongToShort() {
        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setLongUrl("http://baidu.com");
        shortUrlDto.setValidDate(7L);
        ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
        System.out.println(shortUrlResult.getShortUrl());
    }

    @Test
    public void testShortToLong() {
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong("/fxSUH");
        System.out.println(shortUrlResult.getLongUrl());
    }

}
