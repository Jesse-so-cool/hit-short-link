package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.redis.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.standard.utils.ResponseBeanUtil;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 *
 * @author jesse hsj
 * @date 2020/8/18 16:44
 */
@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class RedisTest extends AbstractApplicationTestNg {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void rpush(){
        for (int i = 0; i < 10; i++) {
            ResponseBean scBean = ResponseBeanUtil.createScBean();
            scBean.setData(i);
            redisUtils.push("jesse-demo", JSONObject.toJSONString(scBean));
        }
    }
    @Test
    public void luaTest(){
        List<String> strings = redisUtils.batchPopList("jesse-demo", 100);
        for (String string : strings) {
            ResponseBean responseBean = JSONObject.parseObject(string, ResponseBean.class);
            System.out.println(responseBean.getData());
        }
    }

    @Test
    public void removeTest(){
        List<String> strings = redisUtils.batchPopList("record-error-list", 100);

    }

    @Test
    public void testDelete() {
        String key = "fzWv";
        boolean flag = redisUtils.delete(key);
        Assert.assertTrue(flag == true,"与预期结果不符");
    }
}
