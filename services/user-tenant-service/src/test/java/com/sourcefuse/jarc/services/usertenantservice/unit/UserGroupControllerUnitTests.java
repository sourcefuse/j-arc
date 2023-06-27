package com.sourcefuse.jarc.services.usertenantservice.unit;

import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockCurrentUserSession;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockGroup;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.UserGroupServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Create User Group units Tests")
class UserGroupControllerUnitTests {

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private UserGroupsRepository userGroupsRepo;

  @InjectMocks
  private UserGroupServiceImpl userGroupService;

  private UserGroup userGroup;
  private Group group;
  private UUID mockGroupId;

  @BeforeEach
  public void setup() {
    //prepare the test data
    userGroup = MockGroup.getUserGroupObj();

    group = MockGroup.getGroupObj();
    group.setTenantId(userGroup.getTenantId());
    userGroup.setGroup(group);

    mockGroupId = MockGroup.GROUP_ID;
    MockitoAnnotations.openMocks(this);
    //Set Current LoggedIn User
    MockCurrentUserSession.setCurrentLoggedInUser(null, null, null);
  }

  @Test
  @DisplayName("Test createUserGroup - Success")
  void testCreateUserGroupSuccess() {
    // Mock input data

    UserGroup userGroup = this.userGroup;
    // Create a mock savedGroup
    Group savedGroup = this.group;
    // Create a mock savedUserGroup
    UserGroup savedUserGroup = this.userGroup;
    savedUserGroup.setModifiedOn(savedGroup.getModifiedOn());

    // Mock repository methods
    Mockito
      .when(groupRepository.findById(mockGroupId))
      .thenReturn(Optional.of(savedGroup));
    Mockito
      .when(userGroupsRepo.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.empty());
    Mockito.when(userGroupsRepo.save(userGroup)).thenReturn(savedUserGroup);
    Mockito.when(groupRepository.save(savedGroup)).thenReturn(savedGroup);

    // Call the method under test
    UserGroup result = userGroupService.createUserGroup(userGroup, mockGroupId);

    // Assertions
    Assertions.assertNotNull(result);
    Assertions.assertEquals(savedUserGroup, result);

    // Verify repository method invocations
    Mockito.verify(groupRepository, Mockito.times(1)).findById(mockGroupId);
    Mockito
      .verify(userGroupsRepo, Mockito.times(1))
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito.verify(userGroupsRepo, Mockito.times(1)).save(userGroup);
    Mockito.verify(groupRepository, Mockito.times(1)).save(savedGroup);
  }

  @Test
  @DisplayName("Test createUserGroup - Group not found")
  void testCreateUserGroupGroupNotFound() {
    // Mock input data
    UserGroup userGroup = this.userGroup;

    // Mock repository method
    Mockito
      .when(groupRepository.findById(mockGroupId))
      .thenReturn(Optional.empty());

    // Call the method under test and assert the exception
    Assertions.assertThrows(
      ResponseStatusException.class,
      () -> userGroupService.createUserGroup(userGroup, mockGroupId)
    );

    // Verify repository method invocations
    Mockito.verify(groupRepository, Mockito.times(1)).findById(mockGroupId);
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .save(ArgumentMatchers.any());
    Mockito
      .verify(groupRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Test createUserGroup - UserGroup already exists")
  void testCreateUserGroupUserGroupExists() {
    // Mock input data

    UserGroup userGroup = this.userGroup;

    // Create a mock savedUserGroup
    UserGroup savedUserGroup = this.userGroup;

    // Mock repository methods
    Mockito
      .when(groupRepository.findById(mockGroupId))
      .thenReturn(Optional.of(this.group));
    Mockito
      .when(userGroupsRepo.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(savedUserGroup));

    // Call the method under test and assert the exception

    userGroupService.createUserGroup(userGroup, mockGroupId);

    // Verify repository method invocations
    Mockito.verify(groupRepository, Mockito.times(1)).findById(mockGroupId);
    Mockito
      .verify(userGroupsRepo, Mockito.times(1))
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .save(ArgumentMatchers.any());
    Mockito
      .verify(groupRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName(
    "Test createUserGroup - TenantId in Request does not match with existing group tenantId"
  )
  void testCreateUserGroupTenantIdMismatch() {
    // Mock input data
    Group group = this.group;
    group.setTenantId(MockTenantUser.INVALID_ID);
    UserGroup userGroup = this.userGroup;

    // Mock repository methods
    Mockito
      .when(groupRepository.findById(mockGroupId))
      .thenReturn(Optional.of(group));

    // Call the method under test and assert the exception

    Assertions.assertThrows(
      ResponseStatusException.class,
      () -> userGroupService.createUserGroup(userGroup, mockGroupId)
    );

    // Verify repository method invocations
    Mockito.verify(groupRepository, Mockito.times(1)).findById(mockGroupId);
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .save(ArgumentMatchers.any());
    Mockito
      .verify(groupRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Test updateAllUserGroup: Success")
  void testUpdateAllUserGroupSuccess() {
    // Mock input data
    UUID userGroupId = MockGroup.USER_GROUP_ID;
    UserGroup userGroup = this.userGroup;

    MockCurrentUserSession
      .getCurrentUser()
      .setTenantId(this.group.getTenantId());
    Group group = this.group;
    UserGroup savedUserGroup = this.userGroup;

    Mockito
      .when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(group));

    Mockito
      .when(userGroupsRepo.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(savedUserGroup));

    Mockito
      .when(userGroupsRepo.save(ArgumentMatchers.any()))
      .thenReturn(savedUserGroup);

    Mockito
      .when(groupRepository.save(ArgumentMatchers.any()))
      .thenReturn(group);

    // Call the method
    userGroupService.updateAllUserGroup(mockGroupId, userGroup, userGroupId);

    // Verify the interactions
    Mockito
      .verify(groupRepository, Mockito.times(1))
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.times(1))
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.times(1))
      .save(ArgumentMatchers.any());
    Mockito
      .verify(groupRepository, Mockito.times(1))
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Test updateAllUserGroup: Group Not Found")
  void testUpdateAllUserGroupGroupNotFound() {
    // Mock input data

    UUID userGroupId = MockGroup.USER_GROUP_ID;
    UserGroup userGroup = this.userGroup;

    MockCurrentUserSession.getCurrentUser().setTenantId(group.getTenantId());
    // Mock groupRepository.findOne() to return Optional.empty()
    Mockito
      .when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.empty());

    // Call the method and assert the exception
    Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        userGroupService.updateAllUserGroup(mockGroupId, userGroup, userGroupId)
    );

    // Verify the interactions
    Mockito
      .verify(groupRepository, Mockito.times(1))
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .save(ArgumentMatchers.any());
    Mockito
      .verify(groupRepository, Mockito.never())
      .save(ArgumentMatchers.any());
  }

  @Test
  @DisplayName(
    "Update User Group:Owner cannot be set to null/Empty ->team need at least one owner "
  )
  void testUpdateUserGroup_OneOwner() throws Exception {
    Mockito
      .when(groupRepository.findById(mockGroupId))
      .thenReturn(Optional.of(group));
    Mockito
      .when(userGroupsRepo.count(ArgumentMatchers.any(Specification.class)))
      .thenReturn(1L);
    Mockito
      .when(userGroupsRepo.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(userGroup));

    UserGroup updatedUserGroup = new UserGroup();
    updatedUserGroup.setId(mockGroupId);
    updatedUserGroup.setOwner(false);

    MockCurrentUserSession.getCurrentUser().setTenantId(group.getTenantId());
    Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        userGroupService.updateAllUserGroup(mockGroupId, userGroup, mockGroupId)
    );

    // Verify the calls to the repositories
    Mockito.verify(userGroupsRepo, Mockito.never()).save(updatedUserGroup);
    Mockito.verify(groupRepository, Mockito.never()).save(group);
  }

  @Test
  @DisplayName("Update User Group - User Group Not Found")
  void testUpdateUserGroup_UserGroupNotFound() throws Exception {
    UUID userGroupID = MockGroup.USER_GROUP_ID;
    Mockito
      .when(groupRepository.findById(mockGroupId))
      .thenReturn(Optional.of(group));
    Mockito
      .when(userGroupsRepo.count(ArgumentMatchers.any(Specification.class)))
      .thenReturn(1L);
    Mockito
      .when(userGroupsRepo.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.empty());

    MockCurrentUserSession.getCurrentUser().setTenantId(group.getTenantId());
    Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        userGroupService.updateAllUserGroup(mockGroupId, userGroup, userGroupID)
    );

    // Verify that the repository methods were not called
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .save(ArgumentMatchers.any(UserGroup.class));
    Mockito
      .verify(groupRepository, Mockito.never())
      .save(ArgumentMatchers.any(Group.class));
  }

  @Test
  @DisplayName("Delete User Group - Success")
  void testDeleteUsrGrp_Success() throws Exception {
    // Mock userGroupsRepo methods
    Mockito
      .when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(group));
    Mockito
      .when(userGroupsRepo.findAll(ArgumentMatchers.any(Specification.class)))
      .thenReturn(List.of(userGroup));
    Mockito
      .when(userGroupsRepo.count(ArgumentMatchers.any(Specification.class)))
      .thenReturn(2L);

    userGroupService.deleteUserGroup(mockGroupId, userGroup.getId());

    // Verify that the deleteById and save methods were called
    Mockito
      .verify(userGroupsRepo, Mockito.times(1))
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito.verify(groupRepository, Mockito.times(1)).save(group);
  }

  @Test
  @DisplayName("Delete User Group - Group not found")
  void testDeleteUsrGrp_GroupNotFound() throws Exception {
    // Mock Group not found
    Mockito
      .when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.empty());

    Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        userGroupService.deleteUserGroup(mockGroupId, MockGroup.USER_GROUP_ID)
    );

    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito.verify(groupRepository, Mockito.never()).save(group);
  }

  @Test
  @DisplayName("Delete User Group - Forbidden")
  void testDeleteUsrGrp_Forbidden() throws Exception {
    userGroup.setUserTenant(new UserTenant(MockTenantUser.USER_TENANT_ID)); // Set different user tenant

    // Mock userGroupsRepo method
    Mockito
      .when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(group));
    Mockito
      .when(userGroupsRepo.findAll(ArgumentMatchers.any(Specification.class)))
      .thenReturn(List.of(userGroup));
    Mockito
      .when(userGroupsRepo.count(ArgumentMatchers.any(Specification.class)))
      .thenReturn(1L);

    Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        userGroupService.deleteUserGroup(mockGroupId, MockGroup.USER_GROUP_ID)
    );

    //verify deleteById and save never call due to failure
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito.verify(groupRepository, Mockito.never()).save(group);
  }

  @Test
  @DisplayName("Delete User Group - Owner Removal Forbidden")
  void testDeleteUsrGrp_OwnerRemovalForbidden() throws Exception {
    MockCurrentUserSession
      .getCurrentUser()
      .setTenantId(userGroup.getTenantId());
    MockCurrentUserSession
      .getCurrentUser()
      .setUserTenantId(userGroup.getUserTenant().getId());
    userGroup.setOwner(true);

    // Mock userGroupsRepo methods
    Mockito
      .when(groupRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(group));
    Mockito
      .when(userGroupsRepo.findAll(ArgumentMatchers.any(Specification.class)))
      .thenReturn(List.of(userGroup));
    Assertions.assertThrows(
      ResponseStatusException.class,
      () ->
        userGroupService.deleteUserGroup(mockGroupId, MockGroup.USER_GROUP_ID)
    );

    //verify deleteById and save never call due to failure
    Mockito
      .verify(userGroupsRepo, Mockito.never())
      .delete(ArgumentMatchers.any(Specification.class));
    Mockito.verify(groupRepository, Mockito.never()).save(group);
  }
}
