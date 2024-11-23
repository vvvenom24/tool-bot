package venom.toolbot.notify;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 通知处理器工厂，负责构建处理器链
 */
@Component
@RequiredArgsConstructor
public class NotifyHandlerFactory {
    private final BarkNotifyHandler barkNotifyHandler;
    private final WechatNotifyHandler wechatNotifyHandler;
    private final TelegramNotifyHandler telegramNotifyHandler;
    private final EmailNotifyHandler emailNotifyHandler;

    private NotifyHandler chain;

    @PostConstruct
    public void init() {
        // 构建处理器链，按照 Bark -> Wechat -> Telegram -> Email的顺序
        chain = barkNotifyHandler;
        barkNotifyHandler.setNext(wechatNotifyHandler).setNext(telegramNotifyHandler).setNext(emailNotifyHandler);
    }

    public NotifyHandler getNotifyChain() {
        return chain;
    }
}
