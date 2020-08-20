package cn.com.bluemoon.shorturl.init;

import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordDto;
import cn.com.bluemoon.shorturl.dto.ShortUrlQueryRecordEntity;
import cn.com.bluemoon.shorturl.redis.RedisUtils;
import cn.com.bluemoon.shorturl.repository.ShortUrlQueryRecordRepository;
import cn.com.bluemoon.shorturl.servcie.ShortUrlQueryRecordService;
import com.alibaba.fastjson.JSONObject;
import com.bluemoon.pf.standard.bean.ResponseBean;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.gson.Gson;
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

    @ApolloConfig
    private Config config;

    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    ShortUrlQueryRecordService shortUrlQueryRecordService;


    ScheduledExecutorService executor ;

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
                log.info("redis-mysql定时任务开始执行，一次pop出" + amount + "条，每" + second + "秒执行一次");
                if (executor.isShutdown()) {
                    log.info("redis-mysql定时任务被关闭");
                    executor.shutdownNow();
                }
                List<String> list = redisUtils.batchPopList(key, amount);
                //todo 插入mysql
                handler(list);
            } catch (Throwable e) {
                log.error(e.getLocalizedMessage());
            }
        };
        executor.scheduleWithFixedDelay(command, second, second, TimeUnit.SECONDS);
    }

    private void handler(List<String> list) {
        if (list.size()>0){
            List<ShortUrlQueryRecordDto> shortUrlQueryRecordDtoList = new ArrayList<>();
            for (String data : list){
                ShortUrlQueryRecordDto shortUrlQueryRecordDto= JSONObject.parseObject(data, ShortUrlQueryRecordDto.class);
                shortUrlQueryRecordDtoList.add(shortUrlQueryRecordDto);
            }
            shortUrlQueryRecordService.save(shortUrlQueryRecordDtoList);
        }
    }

    @ApolloConfigChangeListener(value = "application")
    public void listener(ConfigChangeEvent changeEvent) {
        boolean isUpdated = false;
        for (String s : changeEvent.changedKeys()) {
            if (s.startsWith("record-task")){
                isUpdated = true;
                break;
            }
        }
        if (!isUpdated) {return;}
        ConfigChange amount = changeEvent.getChange("record-task.amount");
        if (amount != null) {this.setAmount(Integer.parseInt(amount.getNewValue()));}
        ConfigChange second = changeEvent.getChange("record-task.second");
        if (second != null) {this.setSecond(Integer.parseInt(second.getNewValue()));}
        ConfigChange enabled = changeEvent.getChange("record-task.enabled");
        if (enabled != null){ this.setEnabled(Boolean.valueOf(enabled.getNewValue()));}

        try {
            if (this.enabled) {
                executor.shutdown();
                Thread.sleep(5000);
                while (executor.isTerminated()){
                    init();
                }
            }else {
                executor.shutdown();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

        log.info("apollo update");

    }
}
