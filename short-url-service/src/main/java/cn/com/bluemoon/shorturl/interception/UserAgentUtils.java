package cn.com.bluemoon.shorturl.interception;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jesse hsj
 * @date 2020/8/28 16:06
 */
public class UserAgentUtils {

    private final static ThreadLocal<String> userAgent = new ThreadLocal<>();

    public static void clear() {
        userAgent.remove();
    }

    public static void setUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("USER-AGENT");
        userAgent.set(ua);
    }

    public static String getUserAgent(){
        return userAgent.get();
    }
}
