package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.service.DeleteTntUserServiceImpl;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantUserService;
import com.sourcefuse.jarc.services.usertenantservice.service.UpdateTntUserServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/tenants/{id}/users")
@RequiredArgsConstructor
/*** pending this class created for future to support
Tenant user controller where one Tenant
cannot see other Tenant data */
public class TenantUserController {

  private final TenantUserService tnUsrService;
  UpdateTntUserServiceImpl updateTntUserService;
  DeleteTntUserServiceImpl deleteTntUserService;

  @Value("${tenant.id.not.specified}")
  private String tenantIdNotSpecified;

  @PersistenceContext
  private EntityManager em;

  @PostMapping("")
  public ResponseEntity<Object> createUserTenants(
    @Valid @RequestBody UserDto userDto,
    @PathVariable("id") UUID id
  ) {
    if (id == null) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        tenantIdNotSpecified
      );
    }

    userDto.setTenantId(id);
    userDto
      .getUserDetails()
      .setEmail(userDto.getUserDetails().getEmail().toLowerCase());
    userDto
      .getUserDetails()
      .setUsername(userDto.getUserDetails().getUsername().toLowerCase());
    /** map created for ruling with options */
    Map<String, String> option = new HashMap<>();
    option.put("authId", userDto.getAuthId());
    option.put("authProvider", userDto.getAuthProvider());
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
    tnUsrService.create(userDto, currentUser, option);

    return new ResponseEntity<>("", HttpStatus.CREATED);
  }

  @GetMapping("")
  public ResponseEntity<Object> getUserTenantByRole(
    @PathVariable("id") UUID id
  ) {
    IAuthUserWithPermissions currentUser = getiAuthUserWithPermissions(id);
    Predicate predicate = null;
    Map<String, Object> map = new HashMap<>();
    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_TENANT_USER_RESTRICTED.toString()) &&
      currentUser.getTenantId() == id
    ) {
      map =
        tnUsrService.checkViewTenantRestrictedPermissions(
          currentUser,
          predicate,
          UserView.class
        );
      predicate = (Predicate) map.get(CommonConstants.PREDICATE);
    }
    CriteriaBuilder cb = map.get(CommonConstants.BUILDER) != null
      ? (CriteriaBuilder) map.get(CommonConstants.BUILDER)
      : em.getCriteriaBuilder();
    CriteriaQuery<UserView> cq = map.get(CommonConstants.CRITERIA_QUERY) != null
      ? (CriteriaQuery) map.get(CommonConstants.CRITERIA_QUERY)
      : cb.createQuery(UserView.class);
    Root<UserView> root = cq.from(UserView.class);

    if (predicate != null) {
      predicate =
        cb.and(predicate, cb.equal(root.get(CommonConstants.TENANT_ID), id));
    } else {
      predicate = cb.equal(root.get(CommonConstants.TENANT_ID), id);
    }
    cq.where(
      predicate,
      cb.notEqual(root.get("roleType"), CommonConstants.SUPER_ADMIN_ROLE_TYPE)
    );
    //doubt return UserDto
    return new ResponseEntity<>(
      new UserView()/*tnUsrService.getUserView(cq)*/,
      HttpStatus.OK
    );
  }

  @GetMapping("/view-all")
  public ResponseEntity<Object> findAllUsers(@PathVariable("id") UUID id) {
    List<UserView> usrList = tnUsrService.getAllUsers(id, UserView.class);
    //nonRestrictedUserViewRepo ::doubt
    return new ResponseEntity<>(usrList, HttpStatus.OK);
  }

  @GetMapping("/count")
  public ResponseEntity<Object> userTenantCount(@PathVariable("id") UUID id) {
    IAuthUserWithPermissions currentUser = getiAuthUserWithPermissions(id);

    Predicate predicate = null;
    Map<String, Object> map = new HashMap<>();

    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_TENANT_USER_RESTRICTED.toString()) &&
      currentUser.getTenantId() == id
    ) {
      map =
        tnUsrService.checkViewTenantRestrictedPermissions(
          currentUser,
          predicate,
          UserView.class
        );
      predicate = (Predicate) map.get(CommonConstants.PREDICATE);
    }
    CriteriaBuilder cb = map.get(CommonConstants.BUILDER) != null
      ? (CriteriaBuilder) map.get(CommonConstants.BUILDER)
      : em.getCriteriaBuilder();
    CriteriaQuery<UserView> cq = map.get(CommonConstants.CRITERIA_QUERY) != null
      ? (CriteriaQuery) map.get(CommonConstants.CRITERIA_QUERY)
      : cb.createQuery(UserView.class);
    Root<UserView> root = cq.from(UserView.class);

    if (predicate != null) {
      predicate =
        cb.and(predicate, cb.equal(root.get(CommonConstants.TENANT_ID), id));
    } else {
      predicate = cb.equal(root.get(CommonConstants.TENANT_ID), id);
    }
    cq.where(
      predicate,
      cb.notEqual(root.get("roleType"), CommonConstants.SUPER_ADMIN_ROLE_TYPE)
    );
    long userCount;
    userCount = tnUsrService.getUserView(cq).size();

    //nonRestrictedUserViewRepo ::doubt
    return new ResponseEntity<>(
      Count.builder().totalCount(userCount).build(),
      HttpStatus.OK
    );
  }

  private static IAuthUserWithPermissions getiAuthUserWithPermissions(UUID id) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
    if (
      currentUser.getTenantId() != id &&
      !currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_ANY_USER.toString())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    return currentUser;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Object> findAllUsers(
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    IAuthUserWithPermissions currentUser = getiAuthUserWithPermissions(id);
    if (
      currentUser.getId() != userId &&
      currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_OWN_USER.toString())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    UserView userView = tnUsrService.findById(userId, id, UserView.class);
    //nonRestrictedUserViewRepo ::doubt
    return new ResponseEntity<>(userView, HttpStatus.OK);
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<Object> updateUserById(
    @Valid @RequestBody UserView userView,
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

    if (currentUser.getId().equals(userId) && userView.getRoleId() != null) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    if (userView.getUsername() != null) {
      userView.setUsername(userView.getUsername().toLowerCase());
    }

    updateTntUserService.updateById(currentUser, userId, userView, id);

    return new ResponseEntity<>("User PATCH success", HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Object> deleteUserTenantById(
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
    deleteTntUserService.deleteUserById(currentUser, userId, id);
    return new ResponseEntity<>("User DELETE success", HttpStatus.NO_CONTENT);
  }
}
