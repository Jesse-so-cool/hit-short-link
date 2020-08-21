package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordEntity;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.repository.ShortUrlQueryRecordRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class ServiceTestNg extends AbstractApplicationTestNg {
    @Autowired
    private ShortUrlService shortUrlService;

    @Autowired
    private ShortUrlQueryRecordService shortUrlQueryRecordService;

    @Autowired
    private ShortUrlQueryRecordRepository shortUrlQueryRecordRepository;

    private long startTime;

    private long endTime ;    //获取结束时间
    @Test
    public void testLongToShort(){

        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setLongUrl("https://baijiahao.baidu.com/");
        shortUrlDto.setValidDate(7L);
        ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
        System.out.println(shortUrlResult.getShortUrl());
    }
    @Test
    public void testShortToLong(){
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong("fzVwO");
        System.out.println(shortUrlResult.getLongUrl());
    }


    @Autowired
    private RedisTemplate redisTemplate;


    @BeforeMethod
    public void beforMethod(){
         startTime = System.currentTimeMillis();
    }

    @AfterMethod
    public void afterMethod(){
         endTime = System.currentTimeMillis();
         System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

    @Test(description = "批量插入测试saveAll测试")//523ms
    public void testSaveSaveAll(){

        List<ShortUrlQueryRecordEntity> shortUrlQueryRecordEntityList = new ArrayList<ShortUrlQueryRecordEntity>();
        for (int i =0 ; i<22; i++){
            ShortUrlQueryRecordEntity shortUrlQueryRecordEntity = new ShortUrlQueryRecordEntity();
            shortUrlQueryRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shortUrlQueryRecordEntity.setIp("119.75.217.109");
            shortUrlQueryRecordEntity.setLongUrl("https://baijiahao.baidu.com/");
            shortUrlQueryRecordEntityList.add(shortUrlQueryRecordEntity);
        }

        shortUrlQueryRecordRepository.saveAll(shortUrlQueryRecordEntityList);
    }
    @Test(description = "批量插入测试BatchUpdate") //90ms
    public void testSaveBatchUpdate(){

        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<ShortUrlQueryRecordDto>();
        for (int i =0 ; i<22; i++){
            ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
            shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shortUrlQueryRecordDto.setIp("119.75.217.108");
            shortUrlQueryRecordDto.setLongUrl("https://www.baidu.com/");
            shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        }
        shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);

    }
    @Test(description = "批量插入测试BatchUpdate参数太长") //90ms
    public void testSaveBatchUpdateLongParam(){


        String data="https://baijiahao.baidu.com/";
        String longData="https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697";

        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<ShortUrlQueryRecordDto>();
        for (int i =0 ; i<10; i++){
            ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
            shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shortUrlQueryRecordDto.setIp("119.75.217.108");
            if (i==5){
                shortUrlQueryRecordDto.setLongUrl(longData);
            }else {
                shortUrlQueryRecordDto.setLongUrl(data);
            }

            shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        }
        shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);

    }

    @Test(description = "批量插入测试saveAll测试")//523ms
    public void testSaveSaveAllLongParam(){

        List<ShortUrlQueryRecordEntity> shortUrlQueryRecordEntityList = new ArrayList<ShortUrlQueryRecordEntity>();
        String data="https://baijiahao.baidu.com/";
        String longData="https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697";
        for (int i =0 ; i<22; i++){
            ShortUrlQueryRecordEntity shortUrlQueryRecordEntity = new ShortUrlQueryRecordEntity();
            shortUrlQueryRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shortUrlQueryRecordEntity.setIp("119.75.217.109");
            if (i==20){
                shortUrlQueryRecordEntity.setLongUrl(longData);
            }else {
                shortUrlQueryRecordEntity.setLongUrl(data);
            }
            //shortUrlQueryRecordRepository.save(shortUrlQueryRecordEntity);
            shortUrlQueryRecordEntityList.add(shortUrlQueryRecordEntity);
        }

        shortUrlQueryRecordRepository.saveAll(shortUrlQueryRecordEntityList);
    }

    @Test(description = "查询错误信息")
    public void getErrorMsg() {
        ResponseBean responseBean =  shortUrlQueryRecordService.getErrorMsg(0,10);

    }

}
