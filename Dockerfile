FROM docker.m.daocloud.io/library/maven:3.8.6-openjdk-8 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM docker.m.daocloud.io/library/openjdk:8-jdk

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]