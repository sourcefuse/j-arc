package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import java.util.UUID;

public interface UserGroupService {
  UserGroup createUserGroup(UserGroup userGroup, UUID groupId);

  void updateAllUserGroup(UUID id, UserGroup userGroup, UUID userGroupId);

  void deleteUserGroup(UUID groupId, UUID userGroupId);
}
