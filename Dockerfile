FROM openjdk:17

EXPOSE 8080

COPY target/virtualshop.jar virtualshop.jar

ENTRYPOINT ["java", "jar", "/virtualshop.jar"]