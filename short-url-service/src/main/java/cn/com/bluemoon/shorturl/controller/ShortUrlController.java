package cn.com.bluemoon.shorturl.controller;

import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author XuZhuohao
 * @date 2019/9/18
 */
@Controller
@RequestMapping("/v1")
public class ShortUrlController {
    @Autowired
    private ShortUrlService shortUrlService;

    @RequestMapping("/{uri}")
    public void shortRedirect(@PathVariable String uri, HttpServletResponse response) throws IOException {
        final ShortUrlResult shortUrlResult = shortUrlService.shortToLong(uri);
        // 需要以 https://(http://)开头才可以，否则会带有当前域名
        response.sendRedirect(shortUrlResult.getLongUrl());

    }
}
