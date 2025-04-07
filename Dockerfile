FROM openjdk:21-slim

RUN apt-get update && \
    apt-get install -y libgtk-3-0 libxext6 libxrender1 libxtst6 libxi6 libfreetype6 fonts-dejavu-core wget unzip maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml /app
RUN mvn dependency:go-offline

COPY src /app/src

RUN mvn clean package

CMD ["java", "-jar", "target/FunctionCalculator-0.0.1-SNAPSHOT.jar"]
