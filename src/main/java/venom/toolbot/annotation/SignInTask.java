package venom.toolbot.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignInTask {
    /**
     * 任务名称
     */
    String value();
    
    /**
     * 任务基础URL
     */
    String baseUrl();
}
