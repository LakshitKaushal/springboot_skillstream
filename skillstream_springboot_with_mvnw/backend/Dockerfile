# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Install bash (needed for mvnw script)
RUN apk add --no-cache bash

# Set working directory
WORKDIR /app

# Copy entire backend directory
COPY skillstream_springboot_with_mvnw/backend .

# Make mvnw executable and ensure .mvn directory exists
RUN chmod +x mvnw && \
    chmod +x mvnw.cmd && \
    ls -la .mvn || echo ".mvn directory check"

# Build the application
RUN ./mvnw clean package -DskipTests -Dmaven.multiModuleProjectDirectory=/app

# Expose port (Render will set PORT env var)
EXPOSE ${PORT:-5000}

# Run the application
# Use PORT from environment variable (Render sets this automatically)
CMD ["sh", "-c", "java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod"]

