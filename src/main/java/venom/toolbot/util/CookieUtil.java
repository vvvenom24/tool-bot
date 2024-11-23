package venom.toolbot.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class CookieUtil {
    
    public static Map<String, String> parseCookieString(String cookieString) {
        if (StringUtils.isBlank(cookieString)) {
            return Collections.emptyMap();
        }
        
        return Arrays.stream(cookieString.split(";"))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .map(cookie -> cookie.split("=", 2))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                    parts -> parts[0].trim(),
                    parts -> parts[1].trim(),
                    (v1, v2) -> v2  // 如果有重复的 key，保留最后一个值
                ));
    }
}
