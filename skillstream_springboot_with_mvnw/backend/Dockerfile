# Use Maven with OpenJDK 17 base image
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first (for better layer caching)
COPY skillstream_springboot_with_mvnw/backend/pom.xml .

# Copy source code
COPY skillstream_springboot_with_mvnw/backend/src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/skillstream-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will set PORT env var)
EXPOSE ${PORT:-5000}

# Run the application
# Use PORT from environment variable (Render sets this automatically)
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=prod"]

