FROM maven:3.6.3-jdk-11-openj9 as builder
# Build: gcloud builds submit --tag gcr.io/PROJECT/cv19api
# Run: gcloud run deploy --image gcr.io/PROJECT/cv19api --platform managed
# Useful: https://github.com/ahmetb/cloud-run-faq
# Run with ENV VARS gcloud run deploy --image gcr.io/PROJECT/cv19api --platform managed --update-env-vars PROJECT_PATH=api,DB_NAME=cv19api,DB_PASSWORD=,DB_HOST="",SPRING_PROFILES_ACTIVE=prod,DB_USER=cv19api,INSTANCE_CONNECTION_NAME= --add-cloudsql-instances --memory 2G
# Delete service: gcloud run services delete SERVICE

# memory limit: gcloud run services update cv19api --memory 2G
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk11
# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/cv19api*.jar /cv19api.jar

# Run the web service on container startup.
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-Dcom.sun.net.ssl.checkRevocation=false","-Dserver.port=${PORT}","-jar","/cv19api.jar"]
