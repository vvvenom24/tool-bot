package venom.toolbot.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import venom.toolbot.annotation.SignInTask;
import venom.toolbot.enums.TaskStatusEnum;
import venom.toolbot.exception.NodeSeekRuntimeException;
import venom.toolbot.mapper.QdLogMapper;
import venom.toolbot.model.NodeSeekResp;
import venom.toolbot.util.Json;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@SignInTask(value = "nodeseek", baseUrl = "https://www.nodeseek.com")
public class NodeSeekTask extends AbstractSignInTask {

    public NodeSeekTask(Map<String, String> cookies, String loginAccount, String baseUrl, QdLogMapper qdLogMapper) {
        super(cookies, loginAccount, baseUrl, qdLogMapper);
    }

    @Override
    protected void doSignIn() throws IOException {
        Pair<Integer, Integer> result = startSignIn();
        notifyMessage = "NodeSeek 签到成功！获得" + result.getLeft() + "个鸡腿，总计" + result.getRight();
    }

    @Override
    protected String getAppName() {
        return "nodeseek";
    }

    private Pair<Integer, Integer> startSignIn() throws IOException {
        // 构建签到请求
        Connection connection = Jsoup.connect(baseUrl + "/api/attendance?random=true")
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
                .header("Origin", baseUrl)
                .header("Referer", baseUrl + "/board")
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
