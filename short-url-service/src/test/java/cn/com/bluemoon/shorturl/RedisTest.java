package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.redis.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.standard.utils.ResponseBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

/**
 *
 * @author jesse hsj
 * @date 2020/8/18 16:44
 */
public class RedisTest {


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
        List<String> strings = redisUtils.batchPopList("jesse-demo", 1);
        for (String string : strings) {
            ResponseBean responseBean = JSONObject.parseObject(string, ResponseBean.class);
            System.out.println(responseBean.getData());
        }
    }
}
