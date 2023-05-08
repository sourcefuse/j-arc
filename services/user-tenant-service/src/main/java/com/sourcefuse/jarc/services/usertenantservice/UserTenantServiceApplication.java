package com.sourcefuse.jarc.services.usertenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"classpath:message.properties"})
public class UserTenantServiceApplication {

    public static void main(String[] args) {
		 /*NOTE::
    Relations that we used in typescript (By defining the relationships in interfaces and types,
    you can ensure that the objects you are working with conform to
     a specific structure and have the required properties. This can help with type checking
     and can make it easier to work with the objects in your code.)is pending in java*/
        SpringApplication.run(UserTenantServiceApplication.class, args);


    }

}
