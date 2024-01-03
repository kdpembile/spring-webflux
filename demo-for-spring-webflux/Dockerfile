FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY ./movie-info-service/.mvn/ ./movie-info-service/.mvn
COPY ./movie-info-service/mvnw ./movie-info-service/mvnw
COPY ./movie-info-service/pom.xml ./movie-info-service/

COPY ./reactive-programming-using-reactor/pom.xml ./reactive-programming-using-reactor/

COPY ./movie-info-service/src ./movie-info-service/src
COPY ./reactive-programming-using-reactor/src ./reactive-programming-using-reactor/src

CMD ["./mvnw", "clean", "package"]

COPY ./movie-info-service/target/movie-info-service-0.0.1-SNAPSHOT.jar ./

CMD ["java", "-jar", "movie-info-service-0.0.1-SNAPSHOT.jar"]
