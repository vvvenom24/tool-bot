package venom.toolbot.notify;

/**
 * 通知处理器接口
 */
public interface NotifyHandler {
    /**
     * 设置下一个处理器
     * @param nextHandler 下一个处理器
     * @return 处理器链
     */
    NotifyHandler setNext(NotifyHandler nextHandler);

    /**
     * 发送通知
     * @param message 通知消息
     * @return true: 发送成功，false: 发送失败
     */
    boolean sendNotify(String message);
}
