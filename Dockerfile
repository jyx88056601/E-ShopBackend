FROM openjdk:17-jdk-slim
WORKDIR /backend
COPY target/E-ShopBackend-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8080
CMD ["java", "-jar", "backend.jar"]