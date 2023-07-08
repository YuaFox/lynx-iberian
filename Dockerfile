FROM gradle:jdk17 AS build
RUN apt update
RUN apt install npm -y
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN npm i
RUN npm run build

FROM openjdk:17-alpine
RUN apk update && apk upgrade --no-cache
RUN apk add --no-cache ffmpeg fontconfig ttf-dejavu
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/lynxiberian.jar
COPY scripts/app.sh /app/app.sh
WORKDIR /app
CMD ["sh", "app.sh"]
