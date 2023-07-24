FROM eclipse-temurin:17-jre-jammy
COPY target/user-tenant-service-0.0.1-SNAPSHOT.jar user-tenant-service.jar
EXPOSE 9095
ADD entrypoint.sh entrypoint.sh
CMD [ "entrypoint.sh" ]
ENTRYPOINT ["sh"]