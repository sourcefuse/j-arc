FROM eclipse-temurin:17-jre-jammy
COPY target/audit-service-0.0.1-SNAPSHOT.jar audit-service.jar
ADD entrypoint.sh entrypoint.sh
CMD [ "entrypoint.sh" ]
ENTRYPOINT ["sh"]