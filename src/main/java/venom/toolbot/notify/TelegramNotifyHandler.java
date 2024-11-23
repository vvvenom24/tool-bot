package venom.toolbot.notify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramNotifyHandler extends AbstractNotifyHandler {
    @Override
    protected String getPlatformName() {
        return "Telegram";
    }

    @Override
    protected boolean doSend(String message) {
        // TODO: 实现Telegram发送逻辑
        return false;
    }
}
