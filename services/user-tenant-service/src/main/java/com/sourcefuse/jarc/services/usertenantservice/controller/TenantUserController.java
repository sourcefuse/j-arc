package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.commons.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleKey;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantUserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/tenants/{id}/users")
@RequiredArgsConstructor
public class TenantUserController {

    private final TenantUserService<Map> tnUsrService;

    @PostMapping("")
    public ResponseEntity<Object> createUserTenants(@Valid @RequestBody UserDto userDto, @PathVariable("id") UUID id) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        if (id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "${tenant.id.not.specified}");


        userDto.setTenantId(id);
        userDto.getUserDetails().setEmail(userDto.getUserDetails().getEmail().toLowerCase());
        userDto.getUserDetails().setUsername(userDto.getUserDetails().getUsername().toLowerCase());
        /** map created for ruling with options */
        Map<String, String> option = new HashMap<>();
        option.put("authId", userDto.getAuthId());
        option.put("authProvider", userDto.getAuthProvider());
        tnUsrService.create(userDto, currentUser, option);

        return new ResponseEntity<Object>("", HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllUsrTenantByRole(@PathVariable("id") UUID id) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        if (currentUser.getTenantId() != id &&
                !currentUser.getPermissions().contains(PermissionKey.VIEWANYUSER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());
        }
        Predicate predicate = null;
        Map<String, Object> map = null;
        if (currentUser.getPermissions().contains(PermissionKey.ViewTenantUserRestricted.toString()) &&
                currentUser.getTenantId() == id) {
            map = tnUsrService.checkViewTenantRestrictedPermissions(currentUser, predicate,
                    UserView.class);
            predicate = (Predicate) map.get(CommonConstants.predicate);
        }
        CriteriaBuilder cb = (CriteriaBuilder) map.get(CommonConstants.builder);
        CriteriaQuery cq = (CriteriaQuery) map.get(CommonConstants.criteriaQuery);
        Root root = cq.from(UserView.class);

        if (predicate != null) {
            predicate = cb.and(predicate, cb.equal(root.get("tenantId"), id));
        } else predicate = cb.equal(root.get("tenantId"), id);
        cq.where(predicate, cb.notEqual(root.get("roleType"), CommonConstants.superAdminRoleType));
        if (currentUser.getRole().equalsIgnoreCase(RoleKey.SUPERADMIN.toString())) {
            //doubt::   this.nonRestrictedUserViewRepo.find
            List<UserView> userViews = tnUsrService.getUserView(cq);
        }
        List<UserView> userViews = tnUsrService.getUserView(cq);


        return new ResponseEntity<Object>(userViews, HttpStatus.OK); //doubt return UserDto
    }

    @GetMapping("/view-all")
    public ResponseEntity<Object> findAllUsers(@PathVariable("id") UUID id) {

        List<UserView> usrList = tnUsrService.getAllUsers(id, UserView.class);
        //nonRestrictedUserViewRepo ::doubt
        return new ResponseEntity<Object>(usrList, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Object> count(@PathVariable("id") UUID id) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        if (currentUser.getTenantId() != id &&
                !currentUser.getPermissions().contains(PermissionKey.VIEWANYUSER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());
        }

        Predicate predicate = null;
        Map<String, Object> map = null;

        if (currentUser.getPermissions().contains(PermissionKey.ViewTenantUserRestricted.toString()) &&
                currentUser.getTenantId() == id) {
            map = tnUsrService.checkViewTenantRestrictedPermissions(currentUser, predicate,
                    UserView.class);
            predicate = (Predicate) map.get(CommonConstants.predicate);
        }
        CriteriaBuilder cb = (CriteriaBuilder) map.get(CommonConstants.builder);
        CriteriaQuery cq = (CriteriaQuery) map.get(CommonConstants.criteriaQuery);
        Root root = cq.from(UserView.class);

        if (predicate != null) {
            predicate = cb.and(predicate, cb.equal(root.get("tenantId"), id));
        } else predicate = cb.equal(root.get("tenantId"), id);
        cq.where(predicate, cb.notEqual(root.get("roleType"), CommonConstants.superAdminRoleType));
        long userCount;
        if (currentUser.getRole().equalsIgnoreCase(RoleKey.SUPERADMIN.toString())) {
            //doubt::   this.nonRestrictedUserViewRepo.find
            userCount = tnUsrService.getUserView(cq).size();
        } else userCount = tnUsrService.getUserView(cq).size();

        //nonRestrictedUserViewRepo ::doubt
        return new ResponseEntity<Object>(Count.builder().count(userCount).build(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findAllUsers(@PathVariable("id") UUID id, @PathVariable("userId") UUID userId) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        if (currentUser.getTenantId() != id &&
                !currentUser.getPermissions().contains(PermissionKey.VIEWANYUSER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());
        }
        if (currentUser.getId() != userId &&
                currentUser.getPermissions().contains(PermissionKey.ViewOwnUser.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());

        }

        UserView userView = tnUsrService.findById(userId, id, UserView.class);
        //nonRestrictedUserViewRepo ::doubt
        return new ResponseEntity<Object>(userView, HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUserById(@Valid @RequestBody UserView userView,
                                                 @PathVariable("id") UUID id, @PathVariable("userId") UUID userId) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        if (currentUser.getId().equals(userId) && userView.getRoleId() != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());
        }
        if (userView.getUsername() != null) {
            userView.setUsername(userView.getUsername().toLowerCase());
        }
        tnUsrService.updateById(currentUser, userId, userView, id);

        return new ResponseEntity<Object>("User PATCH success", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserTenantById(@PathVariable("id") UUID id, @PathVariable("userId") UUID userId) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        tnUsrService.deleteUserById(currentUser, userId, id);
        return new ResponseEntity<Object>("User DELETE success", HttpStatus.NO_CONTENT);
    }

}
