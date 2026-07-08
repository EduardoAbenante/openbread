FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN useradd -m springuser
USER springuser

COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]