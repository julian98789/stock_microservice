FROM amazoncorretto:17.0.12

WORKDIR /app

COPY build/libs/stock-0.0.1-SNAPSHOT.jar /app/stock-0.0.1-SNAPSHOT.jar

EXPOSE 8080:8080

CMD ["java", "-jar", "stock-0.0.1-SNAPSHOT.jar"]