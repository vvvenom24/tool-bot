package venom.toolbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(5, new ThreadFactory() {
            private int count = 0;
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "sign-in-thread-" + count++);
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
