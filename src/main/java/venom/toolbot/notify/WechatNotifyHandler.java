package venom.toolbot.notify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WechatNotifyHandler extends AbstractNotifyHandler {
    @Override
    protected String getPlatformName() {
        return "Wechat";
    }

    @Override
    protected boolean doSend(String message) {
        // TODO: 实现微信发送逻辑
        return false;
    }
}
