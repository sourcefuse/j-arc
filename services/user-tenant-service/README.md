# @jArc/user-tenant-service

## Prerequisite

Authentication service as it uses the same database tables and no new migration is needed for this service.
## Overview

Microservice for Tenant User Management (user-tenant-service) to manage the Tenant ,Tenant User ,Roles and Groups.
The User-Tenant-Service is a versatile microservice designed for Tenant User Management. It empowers developers to efficiently manage Tenants, Tenant Users, Roles, and Groups in a seamless manner.

### Key Features 

#### Create and Manage Tenant
It allows the user to create and manage the Tenants by providing the required details ,for eg name ,email etc.

#### Create and Manage Tenant User

TenantUserController Apis allow to register/manage the Tenant User by providing the TenantId and other required details.


#### Create and Manage Role & RoleUserTenant

Role & RoleUserTenant Apis allow user to create /manage  Role and register the user against the
specific role based on the requirement.

#### Create and Manage Group  & UserGroup
Group Apis allow the user to create/manage the Groups,UserGroup Apis  allow the user
to register a user/add members to the particular group based on the requirement.

#### Manage  UserGroups
userGroupsApis allow a user to manage the UserGroups for eg View User Group list ,count etc.

"For more details of each of these features you can run the sandbox example and visit the Swagger UI page."
[Home page](http://localhost:9095/swagger-ui/index.html#) 

Create and Manage Tenant -> [swagger UI](http://localhost:9095/swagger-ui/index.html#/tenant-controller)

Create and Manage Tenant User -> [swagger UI](http://localhost:9095/swagger-ui/index.html#/tenant-user-controller)

Create and Manage Role ->  [swagger UI](http://localhost:9095/swagger-ui/index.html#/role-controller)

RoleUserTenant -> [swagger UI](http://localhost:9095/swagger-ui/index.html#/role-user-tenant-controller)

Create and Manage Group -> [swagger UI](http://localhost:9095/swagger-ui/index.html#/group-controller)

userGroup ->> [swagger UI](http://localhost:9095/swagger-ui/index.html#/user-group-controller)

Manage UserGroups ->  [swagger UI](http://localhost:9095/swagger-ui/index.html#/user-groups-controller)
## Usage

- Add the `user-tenant-service` in dependencies (in `pom.xml`).
```xml
    <dependencies>
        ...
        <dependency>
            <groupId>com.sourcefuse.jarc.services</groupId>
            <artifactId>user-tenant-service</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
  ```
- Add `user-tenant-service` package in component scan in your application
 ```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        {
        "com.example",
        "com.sourcefuse.jarc.services.usertenantservice"
        }
    )
public class ExampleApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}
 ```

#### How to start Sandbox example :
1. Clone this repo and go to `sandbox/user-tenant-service-example` directory, In `application.properties` file provide the database configuration.
2. ByDefault the application will run on URL:port (localhost:9095) ,you may change the port using key value pair at application.properties file for eg (server.port=9095).
3. Command to run through command prompt (nohup java -jar "user-tenant-service-example-0.0.1-SNAPSHOT.jar") or run as SpringBoot app through any IDE(STS,Intellij etc)
4. Visit  [swagger ui](http://localhost:9095/swagger-ui/index.html#) to get access of all the Api's and better understanding  of request and response with required fields.
 ### Setting Environment Variables (Application Properties)

This example below shows a common configuration for a PostgreSQL Database running locally.
In `src/main/resources/application.properties`

```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/userTenant
spring.datasource.username=user
spring.datasource.password=pass

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=

spring.jpa.properties.hibernate.auto_quote_keyword=true
spring.jpa.show-sql=true
```
| Name          | Required | Default Value | Description                                                                                                                        |
| ------------- |----------|---------------| ---------------------------------------------------------------------------------------------------------------------------------- |
| spring.datasource.username | Y        |               | Login username of the database. |
| spring.datasource.password | Y        |               | Login password of the database. |
| spring.datasource.url | Y        |               | JDBC URL of the database. |
| spring.jpa.show-sql | N        | false         | Whether to enable logging of SQL statements. |
| spring.jpa.hibernate.ddl-auto | N        |               | DDL mode |
| spring.jpa.properties.hibernat.dialect | Y        |               | Dialect in Hibernate class. |
| spring.liquibase.enabled | N        | true          | Whether to enable Liquibase support. |
|spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation | Y        | false         |Is used in the Spring Framework's Hibernate integration to control the behavior of Large Object (LOB) handling|
|spring.jpa.properties.hibernate.auto_quote_keyword| Y        | false         |The property spring.jpa.properties.hibernate.auto_quote_keyword is used to control the automatic quoting of SQL keywords in Hibernate queries.|
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
