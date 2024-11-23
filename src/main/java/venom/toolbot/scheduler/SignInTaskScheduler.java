package venom.toolbot.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import venom.toolbot.entity.QdAccount;
import venom.toolbot.factory.SignInTaskFactory;
import venom.toolbot.job.AbstractSignInTask;
import venom.toolbot.mapper.QdAccountMapper;
import venom.toolbot.util.CookieUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignInTaskScheduler {
    private final QdAccountMapper taskConfigMapper;
    private final SignInTaskFactory taskFactory;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Random random = new Random();

    // 每天凌晨12点执行
    @Scheduled(cron = "10 0 0 * * ?")
    public void scheduleDailyTasks() {
        log.info("开始调度今日签到任务");
        List<QdAccount> taskConfigs = taskConfigMapper.selectAll();
        for (QdAccount config : taskConfigs) {
            scheduleTask(config);
        }
    }

    private void scheduleTask(QdAccount config) {
        String taskName = config.getAppName();
        try {
            // 计算今天的执行时间
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.with(config.getStartTime());
            LocalDateTime endTime = now.with(config.getEndTime());
            
            // 如果结束时间小于开始时间，说明跨天了，需要将结束时间加一天
            if (endTime.isBefore(startTime)) {
                endTime = endTime.plusDays(1);
            }
            
            // 在开始时间和结束时间之间随机选择一个时间
            long startSeconds = startTime.toEpochSecond(ZoneOffset.UTC);
            long endSeconds = endTime.toEpochSecond(ZoneOffset.UTC);
            long randomSeconds = startSeconds + random.nextLong(endSeconds - startSeconds + 1);
            LocalDateTime executionTime = LocalDateTime.ofEpochSecond(randomSeconds, 0, ZoneOffset.UTC);
            
            // 计算延迟时间
            long delayMillis = Duration.between(now, executionTime).toMillis();
            if (delayMillis < 0) {
                log.error("任务 {} 的计划执行时间 {} 已过期", taskName, executionTime);
                return;
            }
            
            // 创建任务
            Map<String, String> cookies = CookieUtil.parseCookieString(config.getAccountCookie());
            AbstractSignInTask task = taskFactory.createTask(taskName, config.getLoginAccount(), cookies);
            
            // 调度任务
            scheduledExecutorService.schedule(
                () -> {
                    try {
                        log.info("开始执行任务：{}", taskName);
                        task.run();
                    } catch (Throwable e) {
                        log.error("执行任务 {} 时发生异常", taskName, e);
                    }
                },
                delayMillis,
                TimeUnit.MILLISECONDS
            );
            
            log.info("已调度任务 {}，将在 {} 执行", taskName, executionTime);
        } catch (Exception e) {
            log.error("调度任务 {} 时发生异常", taskName, e);
        }
    }
}
