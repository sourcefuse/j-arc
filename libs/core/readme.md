# @jArc/core

## Overview

`jarc-core` is the [application core](https://jeffreypalermo.com/2008/07/the-onion-architecture-part-1/) for the `sourceloop`. It contains

- adapters
- awares
- constants
- dtos
- entitylisteners
- exeption
- filters
- models
- repositories
- utils

that are used throughout the service catalog.

### Usage
- Add the `jarc-core` in dependencies (in `pom.xml`).
  ```xml
    <dependencies>
        ...
        <dependency>
            <groupId>com.sourcefuse.jarc</groupId>
            <artifactId>jarc-core</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
  ```
- Add `jarc-core` pacakge in component scan in your application
 ```java
    package com.example;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.ComponentScan;

    @SpringBootApplication
    @ComponentScan(
    {
        "com.example",
        "com.sourcefuse.jarc.core"
    }
    )
    public class ExampleApplication {

        public static void main(String[] args) {
            SpringApplication.run(ExampleApplication.class, args);
        }

    }
 ```

### Adapters
- refer [this](https://github.com/sourcefuse/j-arc/tree/master/libs/core/src/main/java/com/sourcefuse/jarc/core/adapters) to check all the adapters provided in here.
#### LocalDateTimeTypeAdapter
- The `LocalDateTimeTypeAdapter` class is a custom implementation of the `TypeAdapter` class from the `Gson library`. It is specifically designed to handle `serialization` and `deserialization` of `LocalDateTime` objects.
- Usage 
```java
  Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
    .create();
```

### Constants
- refer [this](https://github.com/sourcefuse/j-arc/tree/master/libs/core/src/main/java/com/sourcefuse/jarc/core/constants) to check all the constants provided in here.

### DTO's
- DTO stands for data transfer object. As the name suggests, a DTO is an object made to transfer data
- The `jarc-core` DTO's includes 
    - `CountResponse`
        - This DTO has only one field which is count.
        - You can use this DTO when you have to send total count of some thing in response.
        - Usage 
        ```java
        @RestController
        public class YourController{
            @Autowire
            YourRepository yourRepository;
            ...
            @GetMapping("/count")
            @PreAuthorize("isAuthenticated()")
            public ResponseEntity<CountResponse> count() {
                Long count = yourRepository.count();
                return new ResponseEntity<>(
                new CountResponse(count),
                HttpStatus.OK
                );
            }
        }
        ```
    - `ErrorDetails`
        - This DTO has three fields which are timestamp, message and details.
        - You can use this DTO when you have to send response containing error message and description.
        - Usage 
        ```java
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            exception.getMessage(),
            webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        ``` 
- refer [this](https://github.com/sourcefuse/j-arc/tree/master/libs/core/src/main/java/com/sourcefuse/jarc/core/dtos) to check all the dtos provided in here.

### Entity Listners
- There Following entity listeners are there in `jarc-core`.
    - AuditLogEntityListener
        - You can use this entity listener for auditing the entity, when actions like insert, update and delete on entity it will insert the log in database
        - Usage  
        ```java
        @Entity
        @EntityListeners(SoftDeleteEntityListener.class)
        public class YourModel{
            // properties
        }
        ```
    - SoftDeleteEntityListener
        - This entity listener is being used in `SoftDeleteEntity`, This entity listener is responsible for adding the `deletedOn` timestamp and `deletedBy` user id when the entity gets soft deleted.
        - refer [this](https://github.com/sourcefuse/j-arc/blob/master/libs/core/src/main/java/com/sourcefuse/jarc/core/models/base/SoftDeleteEntity.java) to check how `SoftDeleteEntityListener` is being used.

### Enums

- Enums helps in defining a set of named constants. Using enums can make it easier to document intent, or create a set of distinct cases for both numeric and string-based enums.

- refer [this](https://github.com/sourcefuse/j-arc/tree/master/libs/core/src/main/java/com/sourcefuse/jarc/core/enums) to check all the dtos provided in here.

### Exeption
- The `jarc-core` exception contains custom exception class and global exception handler.
- `CommonRuntimeException` and `ResourceNotFoundException` these are custom exception class. You can use this exception class to throw the exception
- `GlobalExceptionHandler`: this class catch the various exceptions and send the appropriate response with ErrorDetails as response body and Http Status code to the user. 

### Filter
- You can use `QueryService` for filtering the query result.
- `QueryService` has two methods 
    ```java
    public <T> List<T> executeQuery(String filterJson, Class<T> entityClass) {
        ...
    }
    public <T> List<T> executeQuery(Filter filter, Class<T> entityClass) {
        ...
    }
    ```
- Usage 
```java
    @Autowired
    QueryService queryService;

    /**
     * String query = "{\"where\":{\"name\":{\"eq\":\"User\"}}}"
     */
    public List<User> getUsersByQuery(String query){
        return this.queryService.executeQuery(query, User.class);
    }

    /**
     * 
     * Filter filter = new Filter();
     * Map<String, Object> fieldsOperation = new HashMap<String, Object>();
     * fieldsOperation.put("eq", "User");
     * filter.getWhere().put("name", fieldsOperation);
     * 
     */
    public List<User> getUsersByQuery(Filter filter){
        return this.queryService.executeQuery(filter, User.class);
    }
```
For more example of executeQuery refer QueryService Test cases [here](https://github.com/sourcefuse/j-arc/blob/master/libs/core/src/test/java/com/sourcefuse/jarc/core/QueryServiceTests.java)

### Models
- Base
    -  `UserModifiableEntity`, `BaseEntity`, `SoftDeletedEntity`, `BaseAuthUser`
    - In order to use models provided, in your application:
        - Extend your model class with `UserModifiableEntity` class provided in `jarc-core` that will add following attributes to the model Class 
            - createdBy
            - modifiedBy
            - createdOn
            - modifiedOn
            - deleted
            - deletedBy
            - deletedOn
        - Extend your model class with `SoftDeletedEntity` class provided in `jarc-core` that will add following attributes to the model Class 
            - deleted
            - deletedBy
            - deletedOn  

### Repository
- A `SoftDeleteRepository` is a custom repository implementation that provides soft delete functionality using JPA (Java Persistence API) and the JpaRepository interface. Soft delete refers to the concept of marking records as deleted instead of physically removing them from the database.
- refer [this](https://github.com/sourcefuse/j-arc/blob/master/libs/core/src/main/java/com/sourcefuse/jarc/core/repositories/SoftDeletesRepository.java) for provided methods accessing soft deleted entities or hard deleting.

### Utils
- The CommonUtils class is a utility class that provides common helper methods for various tasks. One of the methods in the CommonUtils class is `getNullProperties(Object source)`.

- The `getNullProperties(Object source)` method is used to retrieve a list of property names that have null values from a given object (source). This method is typically used when you want to identify which properties of an object are currently null.