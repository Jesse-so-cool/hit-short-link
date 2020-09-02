package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.redis.RedisUtils;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class DubboTestNg extends AbstractApplicationTestNg {
    @Reference(version = "3.0.0")
    private ShortUrlService shortUrlService;

    @Reference(version = "3.0.0")
    private ShortUrlQueryRecordService shortUrlQueryRecordService;

    @Autowired
    private RedisUtils redisUtils;


    @Test(description = "清除redis数据，重新导入正确测试数据")
    public void prepareData() {

        redisUtils.delete("record-error-list");
        String data = "https://baijiahao.baidu.com/";
        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<ShortUrlQueryRecordDto>();
        ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
        shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));
        shortUrlQueryRecordDto.setIp("119.75.217.108");
        shortUrlQueryRecordDto.setLongUrl(data);
        shortUrlQueryRecordDto.setShortUrl("fzWv");
        shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);

    }
    @Test(description = "清除redis数据，重新导入错误测试数据")
    public void prepareErrorData() {

        redisUtils.delete("record-error-list");

        String longData = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697";
        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<ShortUrlQueryRecordDto>();
        ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
        shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));
        shortUrlQueryRecordDto.setIp("119.75.217.108");
        shortUrlQueryRecordDto.setLongUrl(longData);
        shortUrlQueryRecordDto.setShortUrl("fzWv");
        shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);

    }


    @Test(description = "长链接转短链接正常访问")
    public void testLongToShort() {
        String longUrl = "http://baidu.com";
        Long validate = 7L;
        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setLongUrl(longUrl);
        shortUrlDto.setValidDate(validate);
        ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
        System.out.println(shortUrlResult.getShortUrl());
        Assert.assertEquals(shortUrlResult.isSuccess(), true, "dubbo调用失败");
    }

    @Test(description = "短链接转长链接正常访问")
    public void testShortToLong() {
        String shortUrl = "fxSUH";
        //返回结果
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong(shortUrl);
        System.out.println(shortUrlResult.getLongUrl());
        //预期结果
        ShortUrlResult expected = new ShortUrlResult();
        expected.setLongUrl("http://t.bm.link/expired.html");
        expected.setShortUrl("fxSUH");
        expected.setResponseMsg("请求成功");
        expected.setSuccess(true);
        Assert.assertEquals(JSONObject.toJSONString(shortUrlResult), JSONObject.toJSONString(expected), "与预期结果不符");
    }

    @Test(description = "短链接转长链接 redis过期，数据库中没有过期情况")
    public void testShortToLongExpire() {

        redisUtils.delete("fzXh");
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong("fzXhe");
        System.out.println(shortUrlResult.getLongUrl());

    }

    @Test(description = "获得异常数据--正常参数范围", enabled = false)
    public void testGetErrorMsg() {
        ResponseBean responseBean = shortUrlQueryRecordService.getErrorMsg(0, 2);
        String res1 = "[{\"createTime\":\"2020-09-01 15:59:03\",\"ip\":\"119.75.217.108\",\"longUrl\":\"https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697\",\"shortUrl\":\"fzWv\"}, {\"createTime\":\"2020-09-01 15:59:03\",\"ip\":\"119.75.217.108\",\"longUrl\":\"https://baijiahao.baidu.com/\",\"shortUrl\":\"fzWv\"}, {\"createTime\":\"2020-09-01 15:59:03\",\"ip\":\"119.75.217.108\",\"longUrl\":\"https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697\",\"shortUrl\":\"fzWv\"}]";
        String res2 = responseBean.getData().toString();
        System.out.println(res2);
        Assert.assertTrue(res1.equals(res2), "与预期结果不符");
    }
    @Test(description = "获得异常数据--异常参数范围")
    public void testGetErrorMsgErrorParam() {
        ResponseBean responseBean = shortUrlQueryRecordService.getErrorMsg(-1, -2);
        int code = responseBean.getResponseCode();
        System.out.println(code);
        Assert.assertTrue(code == -1 , "与预期结果不符");
    }

    @Test(description = "回滚数据检查插入数据库成功测试 --异常参数", dependsOnMethods = {"prepareData"})
    public void testCheckErrorMsgErrorParam() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(-1, true);
        int code = responseBean.getResponseCode();
        System.out.printf("code: " + code + " msg: " + responseBean.getResponseMsg());
        Assert.assertTrue(code == -1, "与预期结果不符");
    }

    @Test(description = "回滚数据检查插入数据库成功测试", dependsOnMethods = {"prepareData"})
    public void testCheckErrorMsgSaveDB() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(1, true);
        int code = responseBean.getResponseCode();
        System.out.printf("code: " + code + " msg: " + responseBean.getResponseMsg());
        Assert.assertTrue(code == 0, "与预期结果不符");
    }

    @Test(description = "回滚数据检查插入数据库失败测试 --异常", dependsOnMethods = {"prepareErrorData"})
    public void testCheckErrorMsg() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(1, true);
        int code = responseBean.getResponseCode();
        System.out.printf("code: " + code + " msg: " + responseBean.getResponseMsg());
        Assert.assertTrue(code == -1, "与预期结果不符");
    }

    @Test(description = "回滚数据删除测试", dependsOnMethods = {"prepareData"})
    public void testCheckErrorMsgRemove() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(1, false);
        int code = responseBean.getResponseCode();
        System.out.printf("code: " + code + " msg: " + responseBean.getResponseMsg());
        Assert.assertTrue(code == 0, "与预期结果不符");
    }

}
