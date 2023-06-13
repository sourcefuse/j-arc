//package com.sourcefuse.jarc.services.notificationservice;
//​
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.server.ResponseStatusException;
//​
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.UUID;
//​
//​
//@ExtendWith(MockitoExtension.class)
//public class GroupControllerTest {
//​
//  @Mock
//  private GroupRepository groupRepository;
//​
//  @Mock
//  private UserGroupsRepository userGroupsRepository;
//​
//  @InjectMocks
//  private GroupController groupController;
//​
//  private Set<ConstraintViolation<Group>> violations;
//  private Validator validator;
//  private Group group;
//  private UserGroup userGroup;
//  private UUID existingId;
//  private MockMvc mockMvc;
//​
//  @BeforeEach
//  public void setup() {
//    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//    validator = factory.getValidator();
//    mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
//    //prepare the test data
//    userGroup = new UserGroup();
//​
//    group = new Group();
//    group.setId(UUID.randomUUID());
//    group.setName("John");
//    group.setDescription("Group Unit Test");
//    group.setPhotoUrl("photo.google.com");
//    group.setGroupType(UserTenantGroupType.TENANT);
//    group.setUserGroups(Arrays.asList(new UserGroup(), new UserGroup()));
//​
//    existingId = UUID.randomUUID();
//  }
//​
//  @Test
//  @DisplayName("Create Groups - Success")
//  public void testCreateGroups_Success() throws Exception {
//    // Mock the necessary objects and methods
//    Group savedGroup = new Group();
//    savedGroup.setId(UUID.randomUUID());
//    savedGroup.setName("Test");
//    extracted();
//​
//    Mockito.when(groupRepository.save(ArgumentMatchers.any(Group.class))).thenReturn(savedGroup);
//    Mockito.when(userGroupsRepository.save(ArgumentMatchers.any(UserGroup.class))).thenReturn(userGroup);
//​
//    // Call the method under test
//    mockMvc.perform(MockMvcRequestBuilders.post("/groups")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(asJsonString(group)))
//            .andExpect(MockMvcResultMatchers.status().isCreated())
//            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedGroup.getId().toString()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(savedGroup.getName()));
//​
//    // Verify the expected interactions and assertions
//    Mockito.verify(groupRepository, Mockito.times(1)).save(ArgumentMatchers.any(Group.class));
//    Mockito.verify(userGroupsRepository, Mockito.times(1)).save(ArgumentMatchers.any(UserGroup.class));
//  }
//​
//  private static void extracted() {
//    UsernamePasswordAuthenticationToken auth =
//      new UsernamePasswordAuthenticationToken(
//        new IAuthUserWithPermissions(),
//        null,
//        null
//      );
//    SecurityContextHolder.getContext().setAuthentication(auth);
//  }
//​
//  @Test
//  @DisplayName("Test : User Not Authenticated ")
//  public void testCreateGroups_UserNotAuthenticated() {
//    // Mock the necessary objects and methods
//    violations = validator.validate(group);
//    Assertions.assertTrue(violations.isEmpty());
//​
//    // Call the method under test
//    Exception exception = Assertions.assertThrows(
//      NullPointerException.class,
//      () -> groupController.createGroups(group)
//    );
//​
//    Assertions.assertEquals(NullPointerException.class, exception.getClass());
//    Assertions.assertEquals(
//      "Cannot invoke \"org.springframework.security.core.Authentication.getPrincipal()" +
//      "\" because the return value of \"org.springframework.security.core.context.SecurityContext.getAuthentication()\"" +
//      " is null",
//      exception.getMessage()
//    );
//​
//    // Verify the expected interactions and assertions
//    Mockito.verify(groupRepository, Mockito.times(1)).save(ArgumentMatchers.any(Group.class));
//    Mockito.verify(userGroupsRepository, Mockito.never()).save(ArgumentMatchers.any(UserGroup.class));
//  }
//​
//  @Test
//  @DisplayName("Test case Should pass for database error")
//  void testCreateGroups_DatabaseError() throws Exception {
//    // Mock a database error when saving the group
//    Mockito.when(groupRepository.save(ArgumentMatchers.any(Group.class)))
//      .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
//​
//    mockMvc.perform(MockMvcRequestBuilders.post("/groups")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(asJsonString(group)))
//            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
//            .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException))
//            .andExpect(result -> Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus()));
//​
//    // Verify that the save method was called
//    Mockito.verify(groupRepository).save(ArgumentMatchers.any(Group.class));
//    // Verify that the save method was never called
//    Mockito.verify(userGroupsRepository, Mockito.never()).save(ArgumentMatchers.any(UserGroup.class));
//  }
//​
//  @Test
//  @DisplayName("Test case Should pass for database error")
//  void testCreateGroups_userGroupsRepo_DatabaseError() throws Exception {
//    // Mock a database error when saving the group
//    Group savedGroup = new Group();
//​
//    extracted();
//​
//   Mockito.when(groupRepository.save(ArgumentMatchers.any(Group.class))).thenReturn(savedGroup);
//    Mockito.when(userGroupsRepository.save(ArgumentMatchers.any(UserGroup.class)))
//      .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
//​
//    mockMvc.perform(MockMvcRequestBuilders.post("/groups")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(asJsonString(group)))
//            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
//            .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException))
//            .andExpect(result -> Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus()));
//    // Verify that the save method was called
//    Mockito.verify(groupRepository).save(ArgumentMatchers.any(Group.class));
//    // Verify that the save method was never called
//    Mockito.verify(userGroupsRepository).save(ArgumentMatchers.any(UserGroup.class));
//  }
//​
//  @Test
//  @DisplayName("Test: case for count success")
//  public void testCount_Success() throws Exception {
//    // Mock the behavior of roleRepository.count()
//    Mockito.when(groupRepository.count()).thenReturn(5L); // Mock a count of 5
//​
//    // Perform the API call
//    mockMvc.perform(MockMvcRequestBuilders.get("/groups/count")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.totalCount").value(5));
//​
//    // Verify that roleRepository.count() was called
//    Mockito.verify(groupRepository).count();
//  }
//​
//  @Test
//  @DisplayName("Test case should pass for 0 count")
//  public void testCount_Empty() throws Exception {
//    // Mock the behavior of roleRepository.count()
//    Mockito.when(groupRepository.count()).thenReturn(0L); // Mock a count of 0
//​
//    // Perform the API call
//    mockMvc.perform(MockMvcRequestBuilders.get("/groups/count")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.totalCount").value(0));
//​
//    // Verify that roleRepository.count() was called
//    Mockito.verify(groupRepository).count();
//  }
//​
//  @Test
//  @DisplayName("Test: countGroup with database error")
//  public void testCountGroup_DatabaseError() throws Exception {
//    // Mock the behavior of roleRepository.save() to throw an exception
//    Mockito.when(groupRepository.count())
//      .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
//​
//    // Perform the API call and catch the exception
//    mockMvc.perform(MockMvcRequestBuilders.get("/groups/count")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
//            .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException))
//            .andExpect(result -> Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus()));
//​
//    // Verify that roleRepository.save() was called
//    Mockito.verify(groupRepository).count();
//  }
//​
//  @Test
//  @DisplayName("Test getAllGroups success")
//  public void testGetAllGroups_Success() throws Exception {
//    // Prepare test data
//    List<Group> groups = Arrays.asList(
//      new Group(UUID.randomUUID()),
//      new Group(UUID.randomUUID()),
//      new Group(UUID.randomUUID())
//    );
//​
//    // Mock the behavior of roleRepository.findAll()
//    Mockito.when(groupRepository.findAll()).thenReturn(groups);
//​
//    // Perform the API call
//    mockMvc.perform(MockMvcRequestBuilders.get("/groups")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3));
//    // Verify that roleRepository.findAll() was called
//    Mockito.verify(groupRepository).findAll();
//  }
//​
//  @Test
//  @DisplayName("Test: getAllGroups Empty Response")
//  public void testGetAllGroup_Empty() throws Exception {
//    // Mock the behavior of roleRepository.findAll()
//    Mockito.when(groupRepository.findAll()).thenReturn(Arrays.asList());
//​
//    // Perform the API call
//    mockMvc.perform(MockMvcRequestBuilders.get("/groups")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(result -> Assertions.assertNotNull(result.getResponse()))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));
//​
//    // Verify that roleRepository.findAll() was called
//    Mockito.verify(groupRepository).findAll();
//  }
//​}
