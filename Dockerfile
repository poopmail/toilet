FROM maven:3-openjdk-11 AS build

COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn dependency:go-offline -B
RUN mvn package


FROM openjdk:11-jre-slim

WORKDIR /toilet
COPY --from=build target/toilet-*.jar ./toilet.jar

CMD ["java", "-jar", "./toilet.jar"]