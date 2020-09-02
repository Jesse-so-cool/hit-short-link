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

    private RedisUtils redisUtils;


    @Test(description = "清除redis数据，重新导入测试数据")
    public void prepareData() {

        redisUtils.delete("record-error-list");
        String data = "https://baijiahao.baidu.com/";
        String longData = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697";
        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<ShortUrlQueryRecordDto>();
        ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
        shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));
        shortUrlQueryRecordDto.setIp("119.75.217.108");
        shortUrlQueryRecordDto.setLongUrl(longData);
     /*   shortUrlQueryRecordDto.setLongUrl(data);*/
        shortUrlQueryRecordDto.setShortUrl("fzWv");
        shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);

    }

    @Test
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
        Assert.assertEquals(JSONObject.toJSONString(shortUrlResult), JSONObject.toJSONString(expected), "与预期结果不符");
    }


    @Test
    public void testGetErrorMsg() {
        ResponseBean responseBean = shortUrlQueryRecordService.getErrorMsg(0, 2);
        String res1 = "[{\"createTime\":\"2020-09-01 15:59:03\",\"ip\":\"119.75.217.108\",\"longUrl\":\"https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697\",\"shortUrl\":\"fzWv\"}, {\"createTime\":\"2020-09-01 15:59:03\",\"ip\":\"119.75.217.108\",\"longUrl\":\"https://baijiahao.baidu.com/\",\"shortUrl\":\"fzWv\"}, {\"createTime\":\"2020-09-01 15:59:03\",\"ip\":\"119.75.217.108\",\"longUrl\":\"https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697\",\"shortUrl\":\"fzWv\"}]";
        String res2 = responseBean.getData().toString();
        System.out.println(res2);
        Assert.assertTrue(res1.equals(res2), "与预期结果不符");
    }

    @Test(description = "回滚数据检查插入数据库成功测试", dependsOnMethods = {"testCheckErrorMsgSaveDB"})
    public void testCheckErrorMsgSaveDB() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(1, true);
        int code = responseBean.getResponseCode();
        System.out.printf("code: " + code + " msg: " + responseBean.getResponseMsg());
        Assert.assertTrue(code == 0, "与预期结果不符");
    }

    @Test(description = "回滚数据检查插入数据库失败测试", dependsOnMethods = {"testCheckErrorMsgSaveDB"})
    public void testCheckErrorMsg() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(1, true);
        int code = responseBean.getResponseCode();
        System.out.printf("code: " + code + " msg: " + responseBean.getResponseMsg());
        Assert.assertTrue(code == -1, "与预期结果不符");
    }

    @Test(description = "回滚数据删除失败测试", dependsOnMethods = {"testCheckErrorMsgSaveDB"})
    public void testCheckErrorMsgRemove() {
        ResponseBean responseBean = shortUrlQueryRecordService.checkErrorMsg(1, false);
        int code = responseBean.getResponseCode();
        System.out.printf("code: " + code + " msg: " + responseBean.getResponseMsg());
        Assert.assertTrue(code == 0, "与预期结果不符");
    }

}
