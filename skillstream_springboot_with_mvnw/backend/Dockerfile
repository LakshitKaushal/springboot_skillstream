# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better caching)
COPY skillstream_springboot_with_mvnw/backend/mvnw .
COPY skillstream_springboot_with_mvnw/backend/.mvn .mvn
COPY skillstream_springboot_with_mvnw/backend/pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Copy source code
COPY skillstream_springboot_with_mvnw/backend/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port (Render will set PORT env var)
EXPOSE ${PORT:-5000}

# Run the application
# Use PORT from environment variable (Render sets this automatically)
CMD ["sh", "-c", "java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod"]

