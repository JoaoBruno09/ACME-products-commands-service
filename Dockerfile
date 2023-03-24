FROM openjdk:19
EXPOSE 8080
ADD target/products-service-commands.jar products-service-commands.jar
ENTRYPOINT [ "java", "-jar", "/products-service-commands.jar"]