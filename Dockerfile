# Base image
FROM openjdk:17-jdk-slim

# Set working directory trong container
WORKDIR /app

# Copy file JAR vào container
COPY target/backend-assignment3-0.0.1-SNAPSHOT.jar app.jar

# Copy file .env
COPY .env.dev /app/.env

# Đọc các biến từ file .env và thiết lập chúng như các biến môi trường
RUN export $(grep -v '^#' /app/.env | xargs)

# Expose port của ứng dụng
EXPOSE 8080

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar"]