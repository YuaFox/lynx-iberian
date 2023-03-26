FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon


FROM openjdk:17-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/lynxiberian.jar
COPY scripts/app.sh /app/app.sh
WORKDIR /app
CMD ["sh", "app.sh"]