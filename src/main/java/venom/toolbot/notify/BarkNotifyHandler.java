package venom.toolbot.notify;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import venom.toolbot.model.BarkNotifyReq;
import venom.toolbot.model.BarkResp;
import venom.toolbot.util.Json;

@Slf4j
@Component
@RequiredArgsConstructor
public class BarkNotifyHandler extends AbstractNotifyHandler {

    private final RestTemplate restTemplate;

    @Value("${bark.push.url}")
    private String barkPushUrl;

    @Value("${bark.push.title}")
    private String barkPushTitle;

    @Value("${bark.push.icon}")
    private String barkPushIcon;

    @Override
    protected String getPlatformName() {
        return "Bark";
    }

    @Override
    protected boolean doSend(String message) {
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");

        // 创建请求体数据
        BarkNotifyReq notifyReq = new BarkNotifyReq(barkPushTitle, message, barkPushIcon);

        // 将请求头和请求体封装到 HttpEntity 对象中
        String body = Json.toJsonString(notifyReq);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        log.debug("推送 Bark 通知: {}", body);

        // 发送 POST 请求
        ResponseEntity<String> response = restTemplate.exchange(barkPushUrl, HttpMethod.POST, entity, String.class);

        // 获取并打印响应结果
        HttpStatusCode httpStatusCode = response.getStatusCode();
        String responseBody = response.getBody();
        log.debug("Bark 响应状态码: {} 响应内容: {}", httpStatusCode, responseBody);
        if (httpStatusCode != HttpStatus.OK || responseBody == null ||
                (Json.parseObject(responseBody, BarkResp.class)).getCode() != 200) {
            log.error("Bark 响应异常：{}", responseBody);
            return false;
        }

        return true;
    }
}
