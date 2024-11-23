package venom.toolbot.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import venom.toolbot.enums.TaskStatusEnum;
import venom.toolbot.exception.NodeSeekRuntimeException;
import venom.toolbot.model.NodeSeekResp;
import venom.toolbot.util.Json;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class NodeSeekTask {

    private static final String BASE_URL = "https://www.nodeseek.com";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0";
    private final Map<String, String> cookies;

    public void run() {
        try {
            Pair<Integer, Integer> result = startSignIn();
        } catch (NodeSeekRuntimeException nodeSeekException) {

        } catch (Exception e) {
            log.error("NodeSeek 签到异常", e);
        }
    }

    private Pair<Integer, Integer> startSignIn() throws IOException {
        // 构建签到请求
        Connection connection = Jsoup.connect(BASE_URL + "/api/attendance?random=true")
                .cookies(cookies)
                .userAgent(USER_AGENT)
                .header("sec-ch-ua", "\"Microsoft Edge\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                // .header("Sec-Fetch-Site", "cross-site")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Dest", "empty")
                // .header("Sec-Fetch-Dest", "script")
                // .header("Sec-Fetch-Dest", "document")
                // .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-User", "?1")
                .header("Origin", BASE_URL)
                .header("Referer", BASE_URL + "/board")
                .method(Connection.Method.POST)
                .timeout(15000)
                .ignoreContentType(true);
        Connection.Response response = connection.execute();
        String responseText = response.body().trim();
        log.debug("签到响应 body: {}", responseText);
        NodeSeekResp resp = Json.parseObject(responseText, NodeSeekResp.class);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess()) ||
                Objects.isNull(resp.getGain()) || Objects.isNull(resp.getCurrent())) {
            log.error("NodeSeek 签到响应异常：{}", responseText);
            throw new NodeSeekRuntimeException(TaskStatusEnum.NODESEEK_FAILURE_1);
        }
        return Pair.of(resp.getGain(), resp.getCurrent());
    }
}
