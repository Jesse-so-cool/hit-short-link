package cn.com.bluemoon.shorturl.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2019/9/19
 */
@Data
@Configuration
public class ShortUrlConfig {
    //读取配置的属性信息@Value()
    @Value("${short.url.domain}")
    private String domain;
}
