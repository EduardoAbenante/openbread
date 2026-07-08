FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN useradd -m springuser
USER springuser

COPY build/libs/*.jar app.jar

EXPOSE 8080

USER root
RUN mkdir -p /home/springuser/uploads && chown -R springuser:springuser /home/springuser/uploads

USER springuser

ENTRYPOINT [ "java", "-jar", "app.jar" ]