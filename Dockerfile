FROM maven:3.8.6-eclipse-temurin-17-alpine
WORKDIR /library-showcase
COPY ./ ./
RUN mvn clean install -Dmaven.test.skip=true
CMD mvn spring-boot:run
