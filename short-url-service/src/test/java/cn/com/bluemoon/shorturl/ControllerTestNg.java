package cn.com.bluemoon.shorturl;

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

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class ControllerTestNg  extends AbstractApplicationTestNg {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeClass
    public void setUp() {
        //单个类,项目拦截器无效
//      mvc = MockMvcBuilders.standaloneSetup(new ProductController()).build();
        //项目拦截器有效
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
        RequestBuilder request = MockMvcRequestBuilders.get("/fxSUx");

        MvcResult mvcResult = mockMvc.perform(request).andReturn() ;
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果："+status);
        System.out.println(content);
        Assert.assertTrue(status==200,"请求失败");
    }

}
