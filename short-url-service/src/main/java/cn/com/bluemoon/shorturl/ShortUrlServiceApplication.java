package cn.com.bluemoon.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * application
 *
 * @author XuZhuohao
 * @date 2019/9/18
 */
@EnableJpaAuditing
@SpringBootApplication
public class ShortUrlServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortUrlServiceApplication.class);
    }
}
