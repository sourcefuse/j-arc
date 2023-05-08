package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserGroupsRepository extends JpaRepository<UserGroup, UUID> {

    void deleteAllByGroupId(UUID groupId);

    Optional<UserGroup> findByGroupIdAndUserTenantId(UUID groupId, UUID userTenantId);

    Long countByGroupIdAndIsOwner(UUID groupId, boolean bool);

    Optional<UserGroup> findByGroupIdAndIdAndIsOwner(UUID groupId, UUID userGrpId, boolean bool);

    Long getUserGroupCountByGroupId(UUID id);

    List<UserGroup> findByGroupIdOrIdOrUserTenantId(UUID id, UUID userGroupId, UUID useTenantID);

    void deleteAllByUserTenantId(UUID id);
}
