package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TenantUserServiceImpl implements TenantUserService {

  @Override
  public UserDto create(
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    Object options
  ) {
    return null;
  }

  @Override
  public Map<String, Object> checkViewTenantRestrictedPermissions(
    IAuthUserWithPermissions currentUser,
    Predicate predicate,
    Class<UserView> cls
  ) {
    return Collections.emptyMap();
  }

  @Override
  public List<UserView> getUserView(CriteriaQuery<UserView> criteriaQuery) {
    return Collections.emptyList();
  }

  @Override
  public List<UserView> getAllUsers(UUID id, Class<UserView> type) {
    return Collections.emptyList();
  }

  @Override
  public List<UserView> count(CriteriaQuery<UserView> cq) {
    return Collections.emptyList();
  }

  @Override
  public UserView findById(UUID userId, UUID id, Class<UserView> type) {
    return null;
  }
}
