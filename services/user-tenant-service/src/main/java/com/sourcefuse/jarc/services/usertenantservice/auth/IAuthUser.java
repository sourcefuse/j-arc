package com.sourcefuse.jarc.services.usertenantservice.auth;

import lombok.Data;

import java.util.UUID;

/*class creating for testing purpose not required for user tenant serv*/
@Data
public class IAuthUser {
    UUID id;

    String userName;


    String password;
}
