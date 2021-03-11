package cn.com.jesse.shortlink.init;

import cn.com.jesse.shortlink.dto.ShortUrlQueryRecordDto;
import cn.com.jesse.shortlink.redis.RedisUtils;
import cn.com.jesse.shortlink.servcie.ShortUrlQueryRecordService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * record-task
 *
 * @author jesse hsj
 * @date 2020/8/14 15:08
 */
@Slf4j
@Component
@Configuration()
@ConditionalOnProperty("record-task.enabled")
@ConfigurationProperties(prefix = "record-task")
public class RecordTask {


    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    ShortUrlQueryRecordService shortUrlQueryRecordService;


    ScheduledExecutorService executor;

    public final static String key = "short-url-record-list";
    private Integer amount;
    private Integer second;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    private Boolean enabled;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    @PostConstruct
    public void init() {

        executor = Executors.newSingleThreadScheduledExecutor();
        Runnable command = () -> {
            try {
                if (executor.isShutdown()) {
                    log.info("redis-mysql定时任务被关闭");
                    executor.shutdownNow();
                }
                List<String> list = redisUtils.batchPopList(key, amount);
                log.info("redis-mysql定时任务开始执行，一次pop出" + amount + "条，每" + second + "秒执行一次 实际pop出" + list.size() + "条");
                handler(list);
            } catch (Throwable e) {
                log.error(e.getLocalizedMessage(), e);
            }
        };
        executor.scheduleWithFixedDelay(command, second, second, TimeUnit.SECONDS);
    }

    private void handler(List<String> list) {
        if (list.size() > 0) {
            List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<>();
            for (String data : list) {
                ShortUrlQueryRecordDto shortUrlQueryRecordDto = JSONObject.parseObject(data, ShortUrlQueryRecordDto.class);
                shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
                //考虑redis7天设计的合理
                redisUtils.setData(shortUrlQueryRecordDto.getShortUrl(), shortUrlQueryRecordDto.getLongUrl(), (long) 7);
            }
            shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);
        }
    }


}
