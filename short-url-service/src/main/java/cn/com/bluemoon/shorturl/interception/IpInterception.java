package cn.com.bluemoon.shorturl.interception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取ip
 *
 * @author jesse hsj
 * @date 2020/8/13 16:49
 */
@Slf4j
@Configuration
public class IpInterception  extends WebMvcConfigurationSupport implements HandlerInterceptor {

    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("X-REAL-IP");
        log.info(header);
        threadLocal.set(header);
        return true;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/*");
        super.addInterceptors(registry);
    }
}
