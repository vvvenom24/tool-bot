package venom.toolbot.job;

import lombok.extern.slf4j.Slf4j;
import venom.toolbot.entity.QdLog;
import venom.toolbot.exception.TaskRuntimeException;
import venom.toolbot.mapper.QdLogMapper;

import java.io.IOException;
import java.util.Map;

@Slf4j
public abstract class AbstractSignInTask {
    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0";
    protected final Map<String, String> cookies;
    protected final String loginAccount;
    protected final String baseUrl;
    protected final QdLogMapper qdLogMapper;
    protected QdLog qdLog = null;

    protected String notifyMessage = null;

    protected AbstractSignInTask(Map<String, String> cookies, String loginAccount, String baseUrl, QdLogMapper qdLogMapper) {
        this.cookies = cookies;
        this.loginAccount = loginAccount;
        this.baseUrl = baseUrl;
        this.qdLogMapper = qdLogMapper;
    }

    // 模板方法，定义签到的基本流程
    public final void run() {
        try {
            // 执行签到
            doSignIn();
        } catch (Exception e) {
            handleException(e);
        } finally {
            sendNotify();
            saveLog();
        }
    }

    // 具体的签到实现
    protected abstract void doSignIn() throws IOException;

    protected abstract String getAppName();

    // 异常处理
    private void handleException(Exception e) {
        String appName = getAppName();
        log.error("{} 签到失败：{}", appName, e.getMessage(), e);
        if (e instanceof TaskRuntimeException) {
            qdLog.setMessage(e.getMessage());
            notifyMessage = appName + " 签到失败：" + e.getMessage();
        } else {
            qdLog.setMessage("未知异常：" + e.getMessage());
            notifyMessage = appName + " 签到出现未知异常!";
        }
    }

    // 保存日志到数据库
    private void saveLog() {
        if (qdLog == null) {
            log.info("没有签到日志需要写入！");
            return;
        }
        try {
            qdLogMapper.insertSelective(qdLog);
        } catch (Exception e) {
            log.error("保存签到日志失败", e);
        }
    }

    private void sendNotify() {
        if (notifyMessage == null) {
            log.info("没有通知需要发送！");
            return;
        }
        // TODO:发送通知到：email、telegram、wechat
    }
}
