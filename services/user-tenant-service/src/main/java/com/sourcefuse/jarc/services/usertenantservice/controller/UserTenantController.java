package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.HttpError;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.HttpErrors;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.service.RoleUserTenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = {"${api.roles.user-tenant.context.url}"})
@RequiredArgsConstructor
public class UserTenantController {

    private final RoleUserTenantService roleUTService;

    // authentication adn filter pending doubt ::
    @GetMapping("{id}")
    public ResponseEntity<Object> getRoleByID(@PathVariable("id") UUID id) throws HttpError {

        log.info("::::::::::::: Fetch User View based on User - Tenant id:::::::::::::;");
        UserTenant userTenant = roleUTService.findById(id);
        if(userTenant.getTenantId()==null){
            throw HttpErrors.Forbidden(AuthorizeErrorKeys.NotAllowedAccess.getValue());
        }
        if(userTenant.getUserId()==null){
            throw HttpErrors.Forbidden(AuthorizeErrorKeys.NotAllowedAccess.getValue());
        }

        //where and filter pending
        UserView userView=roleUTService.getUserView(id);
        if(userView==null){
            throw HttpErrors.NotFound("User Not found");
        }
        return new ResponseEntity<Object>(userView, HttpStatus.OK);
    }

}
