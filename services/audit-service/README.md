# @jArc/audit-service

## Overview

A LoopBack microservice used for auditing user actions. All the user actions like insert, update and delete can be audited. 

## Usage

- Add the `audit-service` in dependencies (in `pom.xml`).
  ```xml
    <dependencies>
        ...
        <dependency>
            <groupId>com.sourcefuse.jarc.services</groupId>
	        <artifactId>audit-service</artifactId>
	        <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
  ```
- Add `audit-service` pacakge in component scan in your application
 ```java
    package com.example;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.ComponentScan;

    @SpringBootApplication
    @ComponentScan(
    {
        "com.example",
        "com.sourcefuse.jarc.services.auditservice"
    }
    )
    public class ExampleApplication {

        public static void main(String[] args) {
            SpringApplication.run(ExampleApplication.class, args);
        }

    }
 ```
 - Start the application

### Creating Logs

The logs in this service can either be created through the REST endpoint.

All the different types of action that are logged are

```java
public enum AuditActions {
  SAVE("SAVE"),
  UPDATE("UPDATE"),
  DELETE("DELETE");
  private final String name;

  AuditActions(String value) {
    this.name = value;
  }

  public String value() {
    return this.name;
  }

  @Override
  public String toString() {
    return name;
  }
}
```

### Application properties

Do not forget to set Application properties. The examples below show a common configuration for a PostgreSQL Database running locally. (in `src/main/resources/application.properties`).
```properties
  spring.datasource.username=pg_service_user
  spring.datasource.password=pg_service_user_password
  spring.datasource.url=jdbc:postgresql://localhost:5432/notification_db
  spring.jpa.show-sql= false

  spring.jpa.hibernate.ddl-auto= update
  spring.jpa.properties.hibernat.dialect= org.hibernate.dialect.PostgreSQLDialect
  spring.liquibase.enabled= false

```
| Name          | Required | Default Value | Description                                                                                                                        |
| ------------- | -------- | ------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| spring.datasource.username | Y |  | Login username of the database. |
| spring.datasource.password | Y |  | Login password of the database. |
| spring.datasource.url | Y |  | JDBC URL of the database. |
| spring.jpa.show-sql | N | false | Whether to enable logging of SQL statements. |
| spring.jpa.hibernate.ddl-auto | N |  | DDL mode |
| spring.jpa.properties.hibernat.dialect | Y |  | Dialect in Hibernate class. |
| spring.liquibase.enabled | N | true | Whether to enable Liquibase support. |
| app.jwt-secret | Y |  | JWT Secrete should be in Base64 Format |
| app-jwt-expiration-milliseconds | Y |  | JWT token expiration in milliseconds |
| swagger.auth.username | Y |  | Username for accessing swagger URL's |
| swagger.auth.password | Y |  | Password for accessing swagger URL's |

### API Documentation

#### Common Headers

Authorization: Bearer <token> where <token> is a JWT token signed using JWT issuer and secret.
`Content-Type: application/json` in the response and in request if the API method is NOT GET

#### Common Request path Parameters

{version}: Defines the API Version

### Common Responses

200: Successful Response. Response body varies w.r.t API
401: Unauthorized: The JWT token is missing or invalid
403: Forbidden : Not allowed to execute the concerned API
404: Entity Not Found
400: Bad Request (Error message varies w.r.t API)
201: No content: Empty Response

#### API Details

Visit the [OpenAPI spec docs](./openapi.md)
