FROM openjdk:21-slim

# Install required dependencies and clean up properly
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        libfreetype6 fonts-dejavu-core maven fontconfig \
        # For PDF handling in headless mode
        libfontconfig1 libxrender1 libxtst6 && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy only pom.xml first for better build caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Now copy the actual source
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Explicitly set headless mode
ENV JAVA_OPTS="-Djava.awt.headless=true"

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "target/FunctionCalculator-0.0.1-SNAPSHOT.jar"]