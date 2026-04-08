FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -q -DskipTests package

FROM tomcat:9.0.97-jdk11-temurin
WORKDIR /usr/local/tomcat
COPY --from=build /app/target/untitled1.war webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

