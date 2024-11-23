# 构建阶段
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# 安装 git
RUN apt-get update && apt-get install -y git && apt-get clean

# 复制 Maven 包和源代码
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
COPY .git ./.git

# 生成构建信息文件
RUN echo "Branch: $(git rev-parse --abbrev-ref HEAD)" > build.data && \
    echo "Commit: $(git rev-parse HEAD)" >> build.data && \
    echo "Author: $(git log -1 --pretty=format:'%an <%ae>')" >> build.data && \
    echo "Date:   $(git log -1 --pretty=format:'%ad')" >> build.data && \
    echo "$(git log -1 --pretty=format:'%s')" >> build.data && \
    # 构建应用
    ./mvnw clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/"$TZ" /etc/localtime && \
    echo "$TZ" > /etc/timezone && \
    # 创建目录
    mkdir -p /app/logs /app/heapdump && \
    chmod 777 /app/heapdump && \
    chmod 777 /app/logs

# 复制构建产物和构建信息文件
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/build.data ./build.data

# JVM 配置
ENV JAVA_OPTS="\
    --enable-preview \
    -Xmx1024m \
    -Xms256m \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/heapdump/java_pid_%p.hprof \
    -XX:+ExitOnOutOfMemoryError \
    -Xlog:gc*=info:file=/app/heapdump/gc-%t.log:time,uptime,level,tags:filecount=5,filesize=100m \
    -Dfile.encoding=UTF-8 \
    -Duser.timezone=Asia/Shanghai \
    -Dlogging.file.path=/app/logs"

# 使用 shell 脚本来正确处理环境变量
RUN echo '#!/bin/sh\nexec java $JAVA_OPTS -jar app.jar' > /app/entrypoint.sh && \
    chmod +x /app/entrypoint.sh

# 在 ENTRYPOINT 之前添加健康检查
#HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
#    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["/app/entrypoint.sh"]