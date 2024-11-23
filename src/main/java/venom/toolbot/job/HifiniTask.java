package venom.toolbot.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import venom.toolbot.annotation.SignInTask;
import venom.toolbot.enums.TaskStatusEnum;
import venom.toolbot.exception.HifiniRuntimeException;
import venom.toolbot.mapper.QdLogMapper;
import venom.toolbot.model.HifiniResp;
import venom.toolbot.util.Json;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@SignInTask(value = "hifini", baseUrl = "https://www.hifini.com")
public class HifiniTask extends AbstractSignInTask {

    public HifiniTask(Map<String, String> cookies, String loginAccount, String baseUrl, QdLogMapper qdLogMapper) {
        super(cookies, loginAccount, baseUrl, qdLogMapper);
    }

    @Override
    protected void doSignIn() throws IOException {
        String sign = getSignValue();
        if (sign != null) {
            notifyMessage = "Hifini " + startSignIn(sign);
        }
    }

    @Override
    protected String getAppName() {
        return "hifini";
    }

    private String getSignValue() throws IOException {
        Connection connection = Jsoup.connect(baseUrl + "/sg_sign.htm")
                .userAgent(USER_AGENT)
                .cookies(cookies)
                .method(Connection.Method.GET)
                .timeout(15000)
                .ignoreContentType(true);
        Document doc = connection.execute().parse();
        log.debug("Hifini 获取 sign 页面\n：{}", doc);
        String html = doc.html();

        // 使用正则表达式查找sign值
        Pattern pattern = Pattern.compile("var sign = \"([\\da-f]+)\"");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String signValue = matcher.group(1);
            log.debug("获取到sign值: {}", signValue);
            return signValue;
        } else {
            if (html.contains("登录后查看")) {
                throw new HifiniRuntimeException(TaskStatusEnum.HIFINI_FAILURE_1);
            }
            throw new HifiniRuntimeException(TaskStatusEnum.HIFINI_FAILURE_2);
        }
    }

    private String startSignIn(String sign) throws IOException {
        // 构建签到请求
        Connection connection = Jsoup.connect(baseUrl + "/sg_sign.htm")
                .userAgent(USER_AGENT)
                .cookies(cookies)
                .data("sign", sign)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Fetch-User", "?1")
                .header("Origin", baseUrl)
                .header("Referer", baseUrl + "/")
                .method(Connection.Method.POST)
                .timeout(15000)
                .ignoreContentType(true);

        Connection.Response response = connection.execute();
        String responseText = response.body().trim();
        log.debug("Hifini 签到响应: {}", responseText);
        HifiniResp hifiniResp = Json.parseObject(responseText, HifiniResp.class);
        if (Objects.isNull(hifiniResp) || !Objects.equals(hifiniResp.getCode(), "0") ||
                StringUtils.isBlank(hifiniResp.getMessage())) {
            log.error("Hifini 签到响应异常：{}", responseText);
            throw new HifiniRuntimeException(TaskStatusEnum.HIFINI_FAILURE_3);
        }
        return hifiniResp.getMessage();
    }
}
