FROM amazoncorretto:21

# Set the working directory
WORKDIR /app

# Copy the Gradle files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Download and install Gradle
RUN ./gradlew --version

# Copy the project files
COPY . /app

# Build the project
RUN ./gradlew build

RUN chmod +x ./gradlew && ./gradlew --version
# Set the startup command
CMD ["java", "-jar", "build/libs/vrrom-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
