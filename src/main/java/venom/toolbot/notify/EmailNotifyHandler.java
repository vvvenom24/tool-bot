package venom.toolbot.notify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotifyHandler extends AbstractNotifyHandler {
    @Override
    protected String getPlatformName() {
        return "Email";
    }

    @Override
    protected boolean doSend(String message) {
        // TODO: 实现邮件发送逻辑
        return false;
    }
}
