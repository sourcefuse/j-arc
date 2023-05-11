package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TenantUserService {
  UserDto create(
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    Object options
  );

  Map<String, Object> checkViewTenantRestrictedPermissions(
    IAuthUserWithPermissions currentUser,
    Predicate predicate,
    Class<UserView> cls
  );

  List<UserView> getUserView(CriteriaQuery<UserView> criteriaQuery);

  List<UserView> getAllUsers(UUID id, Class<UserView> type);

  List<UserView> count(CriteriaQuery<UserView> cq);

  UserView findById(UUID userId, UUID id, Class<UserView> type);
}
