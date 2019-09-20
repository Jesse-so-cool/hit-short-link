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
    @Value("${short.url.domain}")
    private String domain;
}
