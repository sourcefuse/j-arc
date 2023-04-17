package com.sourcefuse.userintentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@PropertySource("classpath:urls.properties")
public class UserIntentServiceApplication {

    /*NOTE::
    Relations that we used in typescript (By defining the relationships in interfaces and types,
    you can ensure that the objects you are working with conform to
     a specific structure and have the required properties. This can help with type checking
     and can make it easier to work with the objects in your code.)is pending in java*/
    public static void main(String[] args) {
        SpringApplication.run(UserIntentServiceApplication.class, args);
    }

}


