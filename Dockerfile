FROM amazoncorretto:21

# Set the working directory
WORKDIR /app

# Copy the Gradle files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Make gradlew executable and check version
RUN chmod +x ./gradlew
RUN ls -la /app
RUN ./gradlew --version

# Copy the project files
COPY . /app

# Ensure gradlew is still executable
RUN ls -la /app/gradlew

# Build the project
RUN ./gradlew build

# Set the startup command
CMD ["java", "-jar", "build/libs/vrrom-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
