package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantPrefsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * doubt::
 * filter and authentication pending
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = {"${api.user-tenant.prefs.url}"})
public class UserTenantPrefsController {

    @Autowired
    private UserTenantPrefsService userTPService;

    @PostMapping("")
    public ResponseEntity<Object> createTenantPrefs(@Valid @RequestBody UserTenantPrefs userTPrefs) throws ApiPayLoadException {
        UserTenantPrefs savedUTPrefs;
        log.info(" :::::::::::: Creating Roles User Tenant Apis consumed :::::::::::;;;");

        UserTenantPrefs PreExistsTenantPrefs = userTPService.findByUserTenantIdConfKey(userTPrefs.getUserTenantId(),
                userTPrefs.getConfigKey().getValue());

        if (PreExistsTenantPrefs != null) {
            if (userTPrefs.getConfigValue() != null) PreExistsTenantPrefs.setConfigValue(userTPrefs.getConfigValue());
            savedUTPrefs = userTPService.save(PreExistsTenantPrefs);
        } else
            savedUTPrefs = userTPService.save(userTPrefs);

        return new ResponseEntity<>(savedUTPrefs, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllUsTenantPrefs() {

        log.info("::::::::::::: Fetch Array User -Tenant -Prefs   :::::::::::::;");
        List<UserTenantPrefs> lutPrefs = userTPService.findAll();
        return new ResponseEntity<Object>(lutPrefs, HttpStatus.OK);
    }
}
