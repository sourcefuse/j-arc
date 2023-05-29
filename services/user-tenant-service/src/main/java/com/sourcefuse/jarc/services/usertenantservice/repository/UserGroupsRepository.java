package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupsRepository
  extends SoftDeletesRepository<UserGroup, UUID> {
  void deleteAllByGroupId(UUID groupId);

  Optional<UserGroup> findByGroupIdAndUserTenantId(
    UUID groupId,
    UUID userTenantId
  );

  Long countByGroupIdAndIsOwner(UUID groupId, boolean bool);

  Optional<UserGroup> findByGroupIdAndIdAndIsOwner(
    UUID groupId,
    UUID userGrpId,
    boolean bool
  );

  Long countByGroupId(UUID id);

  List<UserGroup> findByGroupIdOrIdOrUserTenantId(
    UUID id,
    UUID userGroupId,
    UUID useTenantID
  );

  void deleteAllByUserTenantId(UUID id);

  List<UserGroup> findAllByGroupId(UUID id);
}
