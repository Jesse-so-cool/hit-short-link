package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.redis.RedisUtils;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class ControllerTestNg  extends AbstractApplicationTestNg {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Reference(version = "3.0.0")
    private ShortUrlService shortUrlService;

    @Reference(version = "3.0.0")
    private ShortUrlQueryRecordService shortUrlQueryRecordService;


    @Autowired
    private RedisUtils redisUtils;

    @BeforeClass
    public void setUp() {
        //单个类,项目拦截器无效
//      mvc = MockMvcBuilders.standaloneSetup(new ProductController()).build();
        //项目拦截器有效
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

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


    @Test
    public void executeLongToShort() throws  Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("longUrl","http://baidu.com");
        jsonObject.put("validDate","7");
        //调用接口，传入添加的用户参数
        RequestBuilder request = MockMvcRequestBuilders.post("/longToShort")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonObject.toString());

        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果："+status);
        System.out.println(content);
        Assert.assertTrue(status==200,"请求失败");
    }

    @Test
    public void executeShortToLong() throws  Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/fzWvJ");
        MvcResult mvcResult = mockMvc.perform(request).andReturn() ;
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果："+status);
        System.out.println(content);
        Assert.assertTrue(status==302,"请求失败");
    }

    @Test(description = "短链接转长链接 redis过期，数据库中没有过期情况")
    public void testShortToLongExpire() throws Exception {

        redisUtils.delete("fzXh");
        RequestBuilder request = MockMvcRequestBuilders.get("/fzXhe");
        MvcResult mvcResult = mockMvc.perform(request).andReturn() ;
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果："+status);
        System.out.println(content);
        Assert.assertTrue(status==302,"请求失败");


    }

    @Test(description = "获得异常数据--正常参数范围"  , dependsOnMethods = {"prepareData"})
    public void testGetErrorMsg() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/errorMsg")
                .param("start","0").param("end","4");
        int resStatus = check(request);
        Assert.assertTrue(0 == resStatus,"请求失败");
    }

    @Test(description = "获得异常数据--异常参数范围" , dependsOnMethods = {"prepareData"})
    public void testGetErrorMsgErrorParam() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/errorMsg").param("start","-1").param("end","4");
        int resStatus = check(request);
        Assert.assertTrue(-1 == resStatus,"请求失败");
    }
    @Test(description = "回滚数据检查插入数据库成功测试 --异常参数" , dependsOnMethods = {"prepareData"})
    public void testCheckErrorMsgErrorParam() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/checkErrorMsg").param("amount","-1").param("flag","true");
        int resStatus = check(request);
        Assert.assertTrue(-1 == resStatus,"请求失败");
    }
    @Test(description = "回滚数据检查插入数据库成功测试", dependsOnMethods = {"prepareData"})
    public void testCheckErrorMsgSaveDB() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/checkErrorMsg").param("amount","1").param("flag","true");
        int resStatus = check(request);
        Assert.assertTrue(0 == resStatus,"请求失败");
    }

    @Test(description = "回滚数据检查插入数据库失败测试 --异常", dependsOnMethods = {"prepareErrorData"})
    public void testCheckErrorMsg() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/checkErrorMsg").param("amount","1").param("flag","true");
        int resStatus = check(request);
        Assert.assertTrue(-1 == resStatus,"请求失败");
    }
    @Test(description = "回滚数据删除测试" , dependsOnMethods = {"prepareData"})
    public void testCheckErrorMsgRemove() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/checkErrorMsg").param("amount","1").param("flag","false");
        int resStatus = check(request);
        Assert.assertTrue(0 == resStatus,"请求失败");
    }

    private int check(RequestBuilder request) throws Exception {
        MvcResult mvcResult = mockMvc.perform(request).andReturn() ;
        String content = mvcResult.getResponse().getContentAsString();
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(content);
        int resStatus = (int) json.get("responseCode");
        System.out.println("返回结果："+resStatus);
        System.out.println(content);
        return resStatus;

    }



}
