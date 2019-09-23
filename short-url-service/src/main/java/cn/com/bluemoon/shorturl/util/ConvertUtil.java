package cn.com.bluemoon.shorturl.util;

public class ConvertUtil {

    public static final String BASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        int targetBase = BASE.length();
        do {
            int i = (int) (num % targetBase);
            sb.append(BASE.charAt(i));
            num /= targetBase;
        } while (num > 0);

        return sb.reverse().toString();
    }
    public static long toBase10(String input) {
        int srcBase = BASE.length();
        long id = 0;
        String r = new StringBuilder(input).reverse().toString();

        for (int i = 0; i < r.length(); i++) {
            int charIndex = BASE.indexOf(r.charAt(i));
            id += charIndex * (long) Math.pow(srcBase, i);
        }

        return id;
    }
}
