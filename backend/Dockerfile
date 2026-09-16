# 使用轻量级 JDK 17 基础镜像
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 复制 Maven 构建出的 JAR 包
COPY target/*.jar app.jar

# 暴露 Spring Boot 默认端口
EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.mvc.pathmatch.matching-strategy=ant-path-matcher", "-jar", "/app/app.jar", "--spring.mvc.pathmatch.matching-strategy=ant-path-matcher"]
