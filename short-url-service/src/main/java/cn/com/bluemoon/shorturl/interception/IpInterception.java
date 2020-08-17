package cn.com.bluemoon.shorturl.interception;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取ip
 *
 * @author jesse hsj
 * @date 2020/8/13 16:49
 */
public class IpInterception implements HandlerInterceptor {

    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("X-REAL-IP");
        threadLocal.set(header);
        return true;
    }
}
