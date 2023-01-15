FROM openjdk:17
RUN mkdir /app
COPY build/libs/lynxiberian-2023.1.jar /app
COPY ./scripts /app
WORKDIR /app
CMD ["sh", "app.sh"]