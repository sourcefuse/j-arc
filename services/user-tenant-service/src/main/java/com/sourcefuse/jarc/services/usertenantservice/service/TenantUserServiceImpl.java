package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

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
    return null;
  }

  @Override
  public List<UserView> getUserView(CriteriaQuery<UserView> criteriaQuery) {
    return null;
  }

  @Override
  public List<UserView> getAllUsers(UUID id, Class<UserView> type) {
    return null;
  }

  @Override
  public List<UserView> count(CriteriaQuery<UserView> cq) {
    return null;
  }

  @Override
  public UserView findById(UUID userId, UUID id, Class<UserView> type) {
    return null;
  }
}
