package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserGroupsServiceImpl implements UserGroupsService {

    private final UserGroupsRepository userGroupsRepo;

    @Override
    public List<UserGroup> findAll() {

        return userGroupsRepo.findAll();
    }

    @Override
    public UserGroup save(UserGroup userGroup) {
        log.info(" ::::  Create User - Groups  ::::");
        return userGroupsRepo.save(userGroup);
    }

    @Override
    public void deleteAllByGroupId(UUID groupId) {
        log.info(" ::::  Delete All  User - Groups  against groupID::::" + groupId);
        userGroupsRepo.deleteAllByGroupId(groupId);
    }

    @Override
    public List<UserGroup> findByGpIDUsrTntId(UserGroup userGroup) {
        log.info(" ::::  Find   User - Groups  against groupID::::" + userGroup.getGroupId() + " USer Tenant ID" + userGroup.getUserTenantId());
        List<UserGroup> usrGrpList = userGroupsRepo.findByGpIDUsrTntId(userGroup.getGroupId(), userGroup.getUserTenantId());
        return usrGrpList;
    }

    @Override
    public List<UserGroup> findByGpIDUsrTntIdIsOwn(UUID groupId, UUID userGrpId, boolean bool) {
        log.info(" ::::  Find   User - Groups  against groupID::::" + groupId +
                " USer group ID" + userGrpId + " is owner " + bool);
        List<UserGroup> usrGrpList = userGroupsRepo.findByGpIDUsrTntIdIsOwn(groupId, userGrpId, bool);
        return usrGrpList;
    }

    @Override
    public Long getUserGroupCount(UUID groupID, boolean bool) {
        return userGroupsRepo.UserGroupCount(groupID, bool);
    }

    @Override
    public void deleteUserGroup(UUID id) {
        userGroupsRepo.deleteById(id);
    }

    @Override
    public Long getUserGroupCountByGroupId(UUID id) {
        return userGroupsRepo.getUserGroupCountByGroupId(id);
    }

    @Override
    public List<UserGroup> findByGpIDUsrTntIdGrpID(UUID id, UUID userGroupId, UUID useTenantID) {
        List<UserGroup> usrGrpList=  userGroupsRepo.findByGpIDUsrTntIdGrpID(id,userGroupId,useTenantID);
        return usrGrpList;
    }

}
