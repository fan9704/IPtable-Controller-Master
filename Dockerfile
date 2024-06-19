FROM openjdk:17

LABEL maintainer="fan9704"

WORKDIR /app
COPY target/*.jar app.jar

# Environment Variables
# Database Config
ENV ELASTICSEARCH_URI=http://127.0.0.1:9200
ENV ELASTICSEARCH_HOST=127.0.0.1
# RabbitMQ Config
ENV RABBIT_MQ_HOST=127.0.0.1
ENV RABBIT_MQ_PORT=5672
ENV RABBIT_MQ_USERNAME=guest
ENV RABBIT_MQ_PASSWORD=guest

EXPOSE 9991

CMD ["java", "-jar", "app.jar"]