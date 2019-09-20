package cn.com.bluemoon.shorturl.controller;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.repository.ShortUrlRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import cn.com.bluemoon.shorturl.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @date 2019/9/18
 */
@Controller
@RequestMapping()
public class ShortUrlController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ShortUrlService shortUrlService;

    @ResponseBody
    @PostMapping("/longToShort")
    public ShortUrlResult longToShort(ShortUrlDto shortUrlDto) {
        return shortUrlService.longToShort(shortUrlDto);
    }

    @RequestMapping("/{uri}")
    public void shortRedirect(@PathVariable String uri, HttpServletResponse response) throws Exception {
        final ShortUrlResult shortUrlResult = shortUrlService.shortToLong(uri);
        if (!shortUrlResult.isSuccess()) {
            throw new Exception(shortUrlResult.getResponseMsg());
        }
        // 需要以 https://(http://)开头才可以，否则会带有当前域名
        response.sendRedirect(shortUrlResult.getLongUrl());
    }
    @Autowired
    private RedisUtils redisUtils;


    @ResponseBody
    @RequestMapping("/expired.html")
    public String expired() throws Exception {
        return "链接已过期";

    }


}
