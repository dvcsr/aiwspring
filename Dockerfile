# LEARNING CONCEPT: Multi-stage Docker build
# This approach creates smaller, more secure production images

# Stage 1: Build stage (temporary container for building the application)
FROM openjdk:17-jdk-alpine AS builder

# LEARNING CONCEPT: Working directory organization
WORKDIR /app

# LEARNING CONCEPT: Layer caching optimization
# Copy dependency files first (changes less frequently)
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# LEARNING CONCEPT: Dependency caching
# Download dependencies in a separate layer for better caching
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# Copy source code (changes more frequently)
COPY src src

# LEARNING CONCEPT: Build optimization
# Build the application (skip tests for faster builds)
RUN ./gradlew build -x test --no-daemon

# Stage 2: Runtime stage (final lightweight container)
FROM openjdk:17-jre-alpine

# LEARNING CONCEPT: Security best practices
# Create non-root user for running the application
RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

# LEARNING CONCEPT: Application directory structure
WORKDIR /app

# LEARNING CONCEPT: Minimal file copying
# Copy only the built JAR from the builder stage
COPY --from=builder /app/build/libs/aiwspring-backend-1.0.0.jar app.jar

# LEARNING CONCEPT: File ownership and security
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring

# LEARNING CONCEPT: Container networking
# Expose the port your application runs on
EXPOSE 8080

# LEARNING CONCEPT: Health check configuration
# Docker health check for container monitoring
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# LEARNING CONCEPT: Application startup
# Use environment variables for configuration
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# LEARNING CONCEPT: Container entrypoint
# Command to run when container starts
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar app.jar"]