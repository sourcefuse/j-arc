FROM eclipse-temurin:17-jre-jammy
COPY target/feature-toggle-service-0.0.1-SNAPSHOT.jar feature-toggle-service.jar
ADD entrypoint.sh entrypoint.sh
CMD [ "entrypoint.sh" ]
ENTRYPOINT ["sh"]