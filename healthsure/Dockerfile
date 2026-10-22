# Stage 1: Build Java Application using Maven
FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /build

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and package executable Spring Boot JAR
COPY src ./src
RUN mvn clean package -DskipTests=true -B

# Stage 2: Minimal, Secure Production Runtime
FROM eclipse-temurin:21-jre-jammy

# Create non-root system user and group
RUN groupadd -r appgroup && useradd -r -g appgroup -u 1001 appuser

WORKDIR /app

# Create upload directory and set ownership
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

# Copy packaged JAR from builder stage
COPY --from=builder --chown=appuser:appgroup /build/target/healthsure-*.jar /app/healthsure.jar

# Switch to non-root user
USER appuser

# Expose internal application port
EXPOSE 8080

# Environment defaults
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

# Docker container healthcheck using Spring Boot Actuator
HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Execute Spring Boot application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/healthsure.jar"]
