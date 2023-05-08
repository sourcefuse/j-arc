package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TenantUserService<T> {
    public UserDto create(UserDto userData, IAuthUserWithPermissions currentUser, T options);

    Map<String, Object> checkViewTenantRestrictedPermissions(IAuthUserWithPermissions currentUser, Predicate predicate
            , Class cls);

    List<UserView> getUserView(CriteriaQuery criteriaQuery);

    List<UserView> getAllUsers(UUID id, Class type);

    List<UserView> count(CriteriaQuery cq);

    UserView findById(UUID userId, UUID id, Class type);

    void updateById(IAuthUserWithPermissions currentUser, UUID userId, @Valid UserView userView, UUID id);

    void deleteUserById(IAuthUserWithPermissions currentUser, UUID userId, UUID id);
}
