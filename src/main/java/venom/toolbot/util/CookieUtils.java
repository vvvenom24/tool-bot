package venom.toolbot.util;

import java.util.HashMap;
import java.util.Map;

public final class CookieUtils {

    private CookieUtils() {}

    public static Map<String, String> parseCookie(String cookieString) {
        Map<String, String> cookieMap = new HashMap<>();
        String[] cookies = cookieString.split("; ");
        for (String cookie : cookies) {
            String[] parts = cookie.split("=", 2);
            if (parts.length == 2) {
                cookieMap.put(parts[0], parts[1]);
            }
        }
        return cookieMap;
    }
}
