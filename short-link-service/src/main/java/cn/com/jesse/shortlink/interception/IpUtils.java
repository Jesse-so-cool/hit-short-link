package cn.com.jesse.shortlink.interception;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jesse hsj
 * @date 2020/8/28 16:06
 */
public class IpUtils {

    private final static ThreadLocal<String> ip = new ThreadLocal<>();

    public static void clearIp() {
        ip.remove();
    }

    public static void setIp(HttpServletRequest request) {
        String forward = request.getHeader("X-Forwarded-For");
        if (forward == null) {
            ip.set(request.getRemoteHost());
            return;
        }
        String[] split = forward.split(",");
        String realIp = split[0];
        ip.set(realIp);
    }

    public static String getIp() {
        return ip.get();
    }
}
