package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;

import java.util.List;
import java.util.UUID;


public interface UserGroupsService {
    List<UserGroup> findAll();


    UserGroup save(UserGroup userGroup);


    void deleteAllByGroupId(UUID groupId);

    List<UserGroup> findByGpIDUsrTntId(UserGroup userGroup);

    List<UserGroup> findByGpIDUsrTntIdIsOwn(UUID groupId, UUID userGrpId, boolean bool);

    Long getUserGroupCount(UUID id, boolean b);

    void deleteUserGroup(UUID id);

    Long getUserGroupCountByGroupId(UUID id);

    List<UserGroup> findByGpIDUsrTntIdGrpID(UUID id, UUID userGroupId, UUID useTenantID);
}
