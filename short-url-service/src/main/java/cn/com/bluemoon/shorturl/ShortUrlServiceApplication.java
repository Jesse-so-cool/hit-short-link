package cn.com.bluemoon.shorturl;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * application
 *
 * @author XuZhuohao
 * @date 2019/9/18
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableDubbo
public class ShortUrlServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortUrlServiceApplication.class);
    }
}
