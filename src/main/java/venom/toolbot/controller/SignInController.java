package venom.toolbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import venom.toolbot.entity.QdAccount;
import venom.toolbot.factory.SignInTaskFactory;
import venom.toolbot.job.AbstractSignInTask;
import venom.toolbot.mapper.QdAccountMapper;
import venom.toolbot.util.CookieUtil;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/sign-in")
@RequiredArgsConstructor
public class SignInController {

    private final SignInTaskFactory taskFactory;

    private final QdAccountMapper accountMapper;

    @GetMapping("/manual/{id}")
    public String manual(@PathVariable("id") Long id) {
        try {
            QdAccount config = accountMapper.selectByPrimaryKey(id);
            // 创建任务
            Map<String, String> cookies = CookieUtil.parseCookieString(config.getAccountCookie());
            AbstractSignInTask task = taskFactory.createTask(config.getAppName(), config.getLoginAccount(), cookies);
            task.run();
        } catch (Exception e) {
            log.error("手动签到失败！", e);
            return "failure";
        }
        return "success";
    }
}
