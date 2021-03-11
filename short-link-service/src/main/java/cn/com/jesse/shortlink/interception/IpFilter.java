package cn.com.jesse.shortlink.interception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author jesse hsj
 * @date 2020/8/28 15:32
 */
@Slf4j
@Component
public class IpFilter implements Filter {

    final static String GET = "GET";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 得到用户个人相关的信息（登陆的用户，用户的语言）
        HttpServletRequest request1 = (HttpServletRequest) request;
        if (!request1.getMethod().equals(GET)) {
            chain.doFilter(request, response);
            return;
        }
        IpUtils.setIp(request1);
        UserAgentUtils.setUserAgent(request1);
        try {
            chain.doFilter(request, response);
        } finally {
            // 由于tomcat线程重用，记得清空
            IpUtils.clearIp();
            UserAgentUtils.clear();
        }
    }

}
