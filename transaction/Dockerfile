# Use a JDK image as the base
FROM openjdk:17-jdk-slim as builder

# Set the working directory in docker
WORKDIR /app

# Copy the entire project including the gradle directory
COPY . .

# Convert Windows line endings to Unix line endings and make the script executable
RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew

# Build the project
RUN ./gradlew bootJar

# Use a new JDK image to reduce the final image size
FROM openjdk:17-jdk-slim

# Copy the built JAR from the previous stage
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=stage"]