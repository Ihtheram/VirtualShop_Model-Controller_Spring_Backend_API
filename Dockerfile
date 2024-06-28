FROM amazoncorretto:17

EXPOSE 8080

COPY target/virtualshop.jar virtualshop.jar

CMD ["java", "-jar", "virtualshop.jar"]