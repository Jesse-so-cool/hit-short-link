package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class ServiceTestNg extends AbstractApplicationTestNg {
    @Autowired
    private ShortUrlService shortUrlService;

    @Test
    public void testLongToShort(){
        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setLongUrl("http://baidu.com");
        shortUrlDto.setValidDate(7L);
        ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
        System.out.println(shortUrlResult.getShortUrl());
    }
    @Test
    public void testShortToLong(){
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong("/fxSU8");
        System.out.println(shortUrlResult.getLongUrl());
    }


    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void luaTest(){
        //String script = "local key = ARGV[1];local end = '-1'" +
                //"local range = redis.call('lrange', key , '0',end);";
//        String script =
//                "local count = redis.call('incr',KEYS[1])\n" +
//                        "if count == 1 then\n" +
//                        "  redis.call('expire',KEYS[1],ARGV[1])\n" +
//                        "end\n" +
//                        " \n" +
//                        "if count > tonumber(ARGV[2]) then\n" +
//                        "  return 0\n" +
//                        "end\n" +
//                        " \n";
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setResultType(List.class);
        String z = "local low = tonumber(ARGV[1]);\n" +
                "local high = tonumber(ARGV[2]);\n" +
                "local data = redis.call('lrange', KEYS[1], 0,-1);\n" +
                "local res = cjson.encode({'1','2'})\n" +
                "return res";
        script.setScriptText(z);
        //script.setScriptSource(new ResourceScriptSource(new
        //        ClassPathResource("redis.lua")));
        ArrayList keys = new ArrayList();
        keys.add("jesse-demo");
        //Object execute =  redisTemplate.execute(script, keys,"qweqe");//String 已经成功
        Object execute =  redisTemplate.execute(script,new StringRedisSerializer(),new FastJsonRedisSerializer(List.class),keys,String.valueOf(0),String.valueOf(-1));

        //Object execute =  redisTemplate.execute(script,new StringRedisSerializer(),new FastJsonRedisSerializer(List.class),keys);
        System.out.println(JSONObject.toJSONString(execute));
    }
}
