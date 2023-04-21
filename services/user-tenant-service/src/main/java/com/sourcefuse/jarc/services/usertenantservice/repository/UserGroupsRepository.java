package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserGroupsRepository extends JpaRepository<UserGroup, UUID> {

    void deleteAllByGroupId(UUID groupId);

    @Query(value = "select * from main.user_groups where group_id=:groupId and user_tenant_id=:userTenantId LIMIT 1" ,nativeQuery = true)
    List<UserGroup> findByGpIDUsrTntId(UUID groupId, UUID userTenantId);

    @Query(value = "select count(*) from main.user_groups where group_id=:groupId and is_owner=:bool" ,nativeQuery = true)
    Long UserGroupCount(UUID groupId, boolean bool);

    @Query(value = "select * from main.user_groups where group_id=:groupId and id=:userGrpId and is_owner=:bool LIMIT 1" ,nativeQuery = true)
    List<UserGroup> findByGpIDUsrTntIdIsOwn(UUID groupId, UUID userGrpId, boolean bool);

    Long getUserGroupCountByGroupId(UUID id);

    @Query(value = "select * from main.user_groups where group_id=:id or (id=:userGroupId or user_tenant_id=:useTenantID LIMIT 1 )"
            ,nativeQuery = true)
    List<UserGroup> findByGpIDUsrTntIdGrpID(UUID id, UUID userGroupId, UUID useTenantID);
}
