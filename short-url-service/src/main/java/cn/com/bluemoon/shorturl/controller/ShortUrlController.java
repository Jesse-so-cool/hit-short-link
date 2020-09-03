package cn.com.bluemoon.shorturl.controller;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.bluemoon.pf.standard.bean.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @date 2019/9/18
 */
@Controller
@RequestMapping()
public class ShortUrlController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ShortUrlService shortUrlService;

    @Autowired
    private ShortUrlQueryRecordService shortUrlQueryRecordService;

    @ResponseBody
    @PostMapping("/longToShort")
    public ShortUrlResult longToShort(@RequestBody ShortUrlDto shortUrlDto) {
        return shortUrlService.longToShort(shortUrlDto);
    }

    @GetMapping("/{uri}")
    public void shortRedirect(@PathVariable String uri, HttpServletResponse response) throws Exception {
        // 需要以 https://(http://)开头才可以，否则会带有当前域名
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong(uri);
        response.sendRedirect(shortUrlResult.getLongUrl());
    }

    @RequestMapping(value = "errorMsg", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBean getErrorMsg(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "10") int end) {
        return shortUrlQueryRecordService.getErrorMsg(start, end);
    }


    /**
     * @description: flag = true  将数据插入数据库； flag = false 删除数据 ；amount 执行数量
     * @author: myl
     * @time: 2020/8/31 11:34
     */

    @RequestMapping(value = "checkErrorMsg", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseBean checkErrorMsg(@RequestParam(defaultValue = "1") int amount, boolean flag) {
        return shortUrlQueryRecordService.checkErrorMsg(amount, flag);
    }

    @ResponseBody
    @GetMapping("/expired.html")
    public String expired() {
        return "链接已过期";
    }

    @ResponseBody
    @GetMapping("/notExist.html")
    public String notExist() {
        return "链接不存在";
    }

}
