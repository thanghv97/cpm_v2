FROM hub.tcbs.com.vn/base/openjdk-8-rhel8-amd64:latest

ARG JAR_DIRECTORY=target
ARG JAR_APP=cpm-0.0.1.jar
ARG JAR_FILE=${JAR_DIRECTORY}/${JAR_APP}
ARG PORT=6868

USER root

# Copy all the static contents to be included in the Docker image
COPY ${JAR_FILE} $JAR_APP
RUN chmod 777 $JAR_APP

EXPOSE ${PORT}

# Show arch
RUN echo "Running on $(uname -m)"

ENTRYPOINT [ "sh", "-c", "java -jar cpm-0.0.1.jar" ]