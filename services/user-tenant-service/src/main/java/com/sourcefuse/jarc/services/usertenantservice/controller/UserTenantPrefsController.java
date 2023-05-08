package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * doubt::
 * filter and authentication pending
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-tenant-prefs")
public class UserTenantPrefsController {
    private final UserTenantPrefsRepository userTPRepository;

    @PostMapping("")
    public ResponseEntity<Object> createTenantPrefs(@Valid @RequestBody UserTenantPrefs userTPrefs) {
        UserTenantPrefs savedUTPrefs;

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser != null && StringUtils.isNotEmpty(currentUser.getUserTenantId().toString()))
            userTPrefs.setUserTenantId(currentUser.getUserTenantId());

        UserTenantPrefs PreExistsTenantPrefs = userTPRepository.findByUserTenantIdAndConfigKey(userTPrefs.getUserTenantId(), userTPrefs.getConfigKey().getValue());

        if (PreExistsTenantPrefs != null) {
            if (userTPrefs.getConfigValue() != null) PreExistsTenantPrefs.setConfigValue(userTPrefs.getConfigValue());
            savedUTPrefs = userTPRepository.save(PreExistsTenantPrefs);
        } else savedUTPrefs = userTPRepository.save(userTPrefs);

        return new ResponseEntity<>(savedUTPrefs, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllUsTenantPrefs() {

        /* filter where logic is pending currently finding all        filter.where = {
                        and: [filter.where ?? {}, {userTenantId: this.currentUser.userTenantId}],
            };*/
        List<UserTenantPrefs> listUtPrefs = userTPRepository.findAll();
        return new ResponseEntity<Object>(listUtPrefs, HttpStatus.OK);
    }
}
