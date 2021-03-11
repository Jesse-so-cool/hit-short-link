package cn.com.jesse.shortlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * application
 *
 * @date 2019/9/18
 */
@SpringBootApplication
@EnableJpaAuditing
public class ShortLinkServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortLinkServiceApplication.class, args);
    }
}
