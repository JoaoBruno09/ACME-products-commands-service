FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY --from=builder /app/target/products-service-commands.jar products-service-commands.jar
ENTRYPOINT ["java", "-jar", "products-service-commands.jar"]