package cn.com.bluemoon.shorturl;

import cn.com.bluemoon.shorturl.dto.ShortUrlDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordEntity;
import cn.com.bluemoon.shorturl.dto.ShortUrlResult;
import cn.com.bluemoon.shorturl.redis.RedisUtils;
import cn.com.bluemoon.shorturl.repository.ShortUrlQueryRecordRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import cn.com.bluemoon.shorturl.servcie.ShortUrlService;
import com.bluemoon.pf.testng.AbstractApplicationTestNg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest(classes = ShortUrlServiceApplication.class)
public class ServiceTestNg extends AbstractApplicationTestNg {
    @Autowired
    private ShortUrlService shortUrlService;

    @Autowired
    private ShortUrlQueryRecordService shortUrlQueryRecordService;

    @Autowired
    private ShortUrlQueryRecordRepository shortUrlQueryRecordRepository;



    @Autowired
    private RedisUtils redisUtils;

    public static void main(String[] args) {
        String url = "file://C:/opt/settings/server.properties";
        String regex = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        System.out.println(Pattern.matches(regex, url));
    }


    @Test
    public void testLongToShort() {
        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setLongUrl("https://www.cnblogs.com/");
        shortUrlDto.setValidDate(7L);
        ShortUrlResult shortUrlResult = shortUrlService.longToShort(shortUrlDto);
        System.out.println(shortUrlResult.getShortUrl());
    }

    @Test
    public void testShortToLong() {
        ShortUrlResult shortUrlResult = shortUrlService.shortToLong("fzXhe");
        System.out.println(shortUrlResult.getLongUrl());
    }



    @Test(description = "批量插入测试saveAll测试", enabled = false)//523ms
    public void testSaveSaveAll() {

        List<ShortUrlQueryRecordEntity> shortUrlQueryRecordEntityList = new ArrayList<ShortUrlQueryRecordEntity>();
        for (int i = 0; i < 23; i++) {
            ShortUrlQueryRecordEntity shortUrlQueryRecordEntity = new ShortUrlQueryRecordEntity();
            shortUrlQueryRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shortUrlQueryRecordEntity.setIp("119.75.217.109");
            shortUrlQueryRecordEntity.setLongUrl("https://baijiahao.baidu.com/");
            shortUrlQueryRecordEntity.setShortUrl("fzWv");
            shortUrlQueryRecordEntityList.add(shortUrlQueryRecordEntity);
        }
        shortUrlQueryRecordRepository.saveAll(shortUrlQueryRecordEntityList);
    }

    @Test(description = "批量插入测试BatchUpdate", enabled = false) //90ms
    public void testSaveBatchUpdate() {

        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<ShortUrlQueryRecordDto>();
        for (int i = 0; i < 22; i++) {
            ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
            shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shortUrlQueryRecordDto.setIp("119.75.217.108");
            shortUrlQueryRecordDto.setLongUrl("https://www.baidu.com/");
            shortUrlQueryRecordDto.setShortUrl("fzWv");
            shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        }
        shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);

    }

    @Test(description = "批量插入测试BatchUpdate参数太长 --异常" )
    public void testSaveBatchUpdateLongParam() {
        String data = "https://baijiahao.baidu.com/";
        String longData = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=B%E7%AB%99&oq=jpa%2520TransactionTemplate&rsv_pq=ab18331e0001ccef&rsv_t=433f9w4%2BYFFvOci918KqT3M0DqYdWZ8ozU4xvtVFCsbf00ZohhWURMkaKC0&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=2697&rsv_sug3=26&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2697";
        List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<ShortUrlQueryRecordDto>();
        for (int i = 0; i < 6; i++) {
            ShortUrlQueryRecordDto shortUrlQueryRecordDto = new ShortUrlQueryRecordDto();
            shortUrlQueryRecordDto.setCreateTime(new Timestamp(System.currentTimeMillis()));
            shortUrlQueryRecordDto.setIp("119.75.217.108");
            if (i % 2== 0) {
                shortUrlQueryRecordDto.setLongUrl(longData);
            } else {
                shortUrlQueryRecordDto.setLongUrl(data);
            }
            shortUrlQueryRecordDto.setShortUrl("fzWv");
            shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
        }
        shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);
    }


}
