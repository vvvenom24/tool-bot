package venom.toolbot.notify;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽象通知处理器
 */
@Slf4j
public abstract class AbstractNotifyHandler implements NotifyHandler {
    protected NotifyHandler nextHandler;

    @Override
    public NotifyHandler setNext(NotifyHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    @Override
    public boolean sendNotify(String message) {
        try {
            boolean success = doSend(message);
            if (success) {
                log.info("[{}] 发送通知成功", getPlatformName());
                return true;
            }
            log.error("[{}] 发送通知失败，尝试下一个平台", getPlatformName());
        } catch (Exception e) {
            log.error("[{}] 发送通知异常", getPlatformName(), e);
        }

        return nextHandler != null && nextHandler.sendNotify(message);
    }

    /**
     * 获取平台名称
     */
    protected abstract String getPlatformName();

    /**
     * 执行具体的发送逻辑
     * @param message 通知消息
     * @return true: 发送成功，false: 发送失败
     */
    protected abstract boolean doSend(String message);
}
