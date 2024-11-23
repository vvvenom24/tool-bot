package venom.toolbot.factory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import venom.toolbot.annotation.SignInTask;
import venom.toolbot.job.AbstractSignInTask;
import venom.toolbot.mapper.QdLogMapper;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class SignInTaskFactory {

    private final ResourcePatternResolver resourcePatternResolver;
    private final QdLogMapper qdLogMapper;

    // 存储任务类型和对应的Class对象
    private final Map<String, TaskInfo> taskRegistry = new HashMap<>();

    @PostConstruct
    public void init() throws Exception {
        // 扫描指定包下的所有类
        String packageSearchPath = "classpath*:venom/toolbot/job/**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                String className = metadataReader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);

                // 检查类是否有SignInTask注解
                SignInTask annotation = clazz.getAnnotation(SignInTask.class);
                if (annotation != null && AbstractSignInTask.class.isAssignableFrom(clazz)) {
                    String taskName = annotation.value().toLowerCase();
                    String baseUrl = annotation.baseUrl();
                    taskRegistry.put(taskName, new TaskInfo(clazz.asSubclass(AbstractSignInTask.class), baseUrl));
                    log.info("注册签到任务: {} -> {}", taskName, clazz.getName());
                }
            }
        }
    }

    public AbstractSignInTask createTask(String taskName, String loginAccount, Map<String, String> cookies) {
        TaskInfo taskInfo = taskRegistry.get(taskName.toLowerCase());
        if (taskInfo == null) {
            throw new IllegalArgumentException("Unknown task name: " + taskName);
        }

        try {
            return taskInfo.taskClass().getConstructor(Map.class, String.class, String.class, QdLogMapper.class)
                    .newInstance(cookies, loginAccount, taskInfo.baseUrl(), qdLogMapper);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create task instance: " + taskName, e);
        }
    }

    private record TaskInfo(Class<? extends AbstractSignInTask> taskClass, String baseUrl) {}
}
