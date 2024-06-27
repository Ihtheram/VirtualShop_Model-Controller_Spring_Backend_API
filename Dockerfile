FROM openjdk:8

EXPOSE 8080

ADD target/virtualshop.jar virtualshop.jar

ENTRYPOINT ["java", "jar", "/virtualshop.jar"]