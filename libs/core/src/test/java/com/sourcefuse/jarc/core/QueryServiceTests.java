package com.sourcefuse.jarc.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.filters.models.Filter;
import com.sourcefuse.jarc.core.filters.models.IncludeRelation;
import com.sourcefuse.jarc.core.filters.services.QueryService;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.core.test.models.Role;
import com.sourcefuse.jarc.core.test.models.User;
import com.sourcefuse.jarc.core.test.repositories.RoleRepository;
import com.sourcefuse.jarc.core.test.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
class QueryServiceTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  QueryService filterService;

  Role tempRole = new Role();
  Role userRole = new Role();
  Role adminRole = new Role();

  User user1 = new User();
  User user2 = new User();
  User user3 = new User();

  Filter filter = new Filter();
  IncludeRelation includeRelation = new IncludeRelation();
  Map<String, Object> fieldsOperation = new HashMap<String, Object>();

  @BeforeEach
  void setUp() {
    TestConstants.clearTables(entityManager);
    TestConstants.setCurrentLoggedInUser();
    this.setUpRole();

    filter = new Filter();
    includeRelation = new IncludeRelation();
    fieldsOperation = new HashMap<String, Object>();
  }

  private void setUpRole() {
    adminRole.setName("Admin");
    adminRole.setPermissions("Get,Update,Find,Delete");
    adminRole = this.roleRepository.save(adminRole);

    userRole.setName("User");
    userRole.setPermissions("Get,Update,Find");
    userRole = this.roleRepository.save(userRole);

    tempRole.setName("Temp");
    tempRole.setPermissions("Get");
    tempRole = this.roleRepository.save(tempRole);

    this.setUpUser();
  }

  private void setUpUser() {
    user1.setFirstName("User One");
    user1.setLastName("Admin");
    user1.setAge(20);
    user1 = this.userRepository.save(user1);

    user2.setFirstName("User Two");
    user2.setLastName("Temp");
    user2.setAge(22);
    user2 = this.userRepository.save(user2);

    user3.setFirstName("User Three");
    user3.setLastName("User");
    user3.setAge(24);
    user3 = this.userRepository.save(user3);
  }

  private List<User> filterWithOperatorAndAge(String operator, Object age) {
    fieldsOperation.put(operator, age);
    filter.getWhere().put("age", fieldsOperation);
    return filterService.executeQuery(filter, User.class);
  }

  @Test
  void testFilter_ForEqualsOperator() {
    fieldsOperation.put("eq", "User");
    filter.getWhere().put("name", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1);

    assertThat(userRole.getId()).isEqualTo(roles.get(0).getId());
  }

  @Test
  void testFilter_ForNotEqualsOperator() {
    fieldsOperation.put("neq", "User");
    filter.getWhere().put("name", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2).contains(adminRole, tempRole);
  }

  @Test
  void testFilter_ForGreaterThanOperator() {
    List<User> users = this.filterWithOperatorAndAge("gt", "20");
    assertThat(users).hasSize(2).contains(user2, user3);
  }

  @Test
  void testFilter_ForGreaterThanOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> this.filterWithOperatorAndAge("gt", "24s")
    );
  }

  @Test
  void testFilter_ForGreaterThanOrEqulsToOperator() {
    List<User> users = this.filterWithOperatorAndAge("gte", "20");

    assertThat(users).hasSize(3).contains(user1, user2, user3);
  }

  @Test
  void testFilter_ForGreaterThanOrEqulsToOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> this.filterWithOperatorAndAge("gte", "24s")
    );
  }

  @Test
  void testFilter_ForLessThanOperator() {
    List<User> users = this.filterWithOperatorAndAge("lt", 24);

    assertThat(users).hasSize(2).contains(user1, user2);
  }

  @Test
  void testFilter_ForLessThanOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> this.filterWithOperatorAndAge("lt", "24s")
    );
  }

  @Test
  void testFilter_ForLessThanOrEqualsToOperator() {
    List<User> users = this.filterWithOperatorAndAge("lte", "24");

    assertThat(users).hasSize(3).contains(user1, user2, user3);
  }

  @Test
  void testFilter_ForLessThanOrEqualsToOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> this.filterWithOperatorAndAge("lte", "24s")
    );
  }

  @Test
  void testFilter_ForLikeOperator() {
    fieldsOperation.put("like", "Update");
    filter.getWhere().put("permissions", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2).contains(adminRole, userRole);
  }

  @Test
  void testFilter_ForNotLikeOperator() {
    fieldsOperation.put("nlike", "Update");
    filter.getWhere().put("permissions", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1);
    assertThat(tempRole.getId()).isEqualTo(roles.get(0).getId());
  }

  @Test
  void testFilter_FieldSelectionTest() {
    filter.getFields().put("name", true);
    fieldsOperation.put("nlike", "Update");
    filter.getWhere().put("permissions", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1);

    assertNull(roles.get(0).getId());
    assertNull(roles.get(0).getPermissions());
    assertThat(tempRole.getName()).isEqualTo(roles.get(0).getName());
  }

  @Test
  void testFilter_ForInOerator() {
    List<UUID> uuids = new ArrayList<>();
    uuids.add(tempRole.getId());
    uuids.add(adminRole.getId());
    fieldsOperation.put("in", uuids);
    filter.getWhere().put("id", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2).contains(tempRole, adminRole);
  }

  @Test
  void testFilter_ForNotInOerator() {
    List<UUID> uuids = new ArrayList<>();
    uuids.add(tempRole.getId());
    uuids.add(adminRole.getId());
    fieldsOperation.put("nin", uuids);
    filter.getWhere().put("id", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1).contains(userRole);
  }

  @Test
  void testFilter_FieldSelectionTestForMultipleRoles() {
    filter.getFields().put("name", true);
    fieldsOperation.put("like", "Update");
    filter.getWhere().put("permissions", fieldsOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2);

    assertNull(roles.get(0).getId());
    assertNull(roles.get(0).getPermissions());
    assertNotNull(roles.get(0).getName());

    assertNull(roles.get(1).getId());
    assertNull(roles.get(1).getPermissions());
    assertNotNull(roles.get(1).getName());
  }

  @Test
  void testFilter_AndOperation() {
    Map<String, Object> fieldsCondOperation = new HashMap<String, Object>();
    fieldsOperation.put("like", "Update");
    fieldsCondOperation.put("permissions", fieldsOperation);
    fieldsCondOperation.put("name", "Admin");
    filter.getWhere().put("and", fieldsCondOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1).contains(adminRole);
  }

  @Test
  void testFilter_OrInsideAndOperation() {
    fieldsOperation.put("like", "Update");
    Map<String, Object> orCondOperation = new HashMap<String, Object>();
    orCondOperation.put("permissions", fieldsOperation);
    orCondOperation.put("name", "Admin");
    Map<String, Object> andCondOperation = new HashMap<String, Object>();
    andCondOperation.put("or", orCondOperation);
    andCondOperation.put("permissions", "Get,Update,Find,Delete");
    filter.getWhere().put("and", andCondOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1).contains(adminRole);
  }

  @Test
  void testFilter_AndInsideOrOperation() {
    fieldsOperation.put("like", "Update");
    Map<String, Object> orCondOperation = new HashMap<String, Object>();
    orCondOperation.put("permissions", fieldsOperation);
    orCondOperation.put("name", "Admin");
    Map<String, Object> andCondOperation = new HashMap<String, Object>();
    andCondOperation.put("and", orCondOperation);
    andCondOperation.put("permissions", "Get");
    filter.getWhere().put("or", andCondOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2).contains(adminRole, tempRole);
  }

  @Test
  void testFilter_OrOperation() {
    Map<String, Object> fieldsCondOperation = new HashMap<String, Object>();
    fieldsOperation.put("like", "Update");
    fieldsCondOperation.put("permissions", fieldsOperation);
    fieldsCondOperation.put("name", "Temp");
    filter.getWhere().put("or", fieldsCondOperation);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(3).contains(adminRole, userRole, tempRole);
  }

  @Test
  void testFilterWithJson_ForEqualsOperator() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"name\":{\"eq\":\"User\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(1);

    assertThat(userRole.getId()).isEqualTo(roles.get(0).getId());
  }

  @Test
  void testFilterWithJson_ForNotEqualsOperator() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"name\":{\"neq\":\"User\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(2).contains(adminRole, tempRole);
  }

  @Test
  void testFilterWithJson_ForGreaterThanOperator() {
    List<User> users = filterService.executeQuery(
      "{\"where\":{\"age\":{\"gt\":\"20\"}}}",
      User.class
    );
    assertThat(users).hasSize(2).contains(user2, user3);
  }

  @Test
  void testFilterWithJson_ForGreaterThanOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () ->
        filterService.executeQuery(
          "{\"where\":{\"age\":{\"gt\":\"24s\"}}}",
          User.class
        )
    );
  }

  @Test
  void testFilterWithJson_ForGreaterThanOrEqulsToOperator() {
    List<User> users = filterService.executeQuery(
      "{\"where\":{\"age\":{\"gte\":\"20\"}}}",
      User.class
    );

    assertThat(users).hasSize(3).contains(user1, user2, user3);
  }

  @Test
  void testFilterWithJson_ForGreaterThanOrEqulsToOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () ->
        filterService.executeQuery(
          "{\"where\":{\"age\":{\"gte\":\"20s\"}}}",
          User.class
        )
    );
  }

  @Test
  void testFilterWithJson_ForLessThanOperator() {
    List<User> users = filterService.executeQuery(
      "{\"where\":{\"age\":{\"lt\":\"24\"}}}",
      User.class
    );

    assertThat(users).hasSize(2).contains(user1, user2);
  }

  @Test
  void testFilterWithJson_ForLessThanOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () ->
        filterService.executeQuery(
          "{\"where\":{\"age\":{\"lt\":\"24s\"}}}",
          User.class
        )
    );
  }

  @Test
  void testFilterWithJson_ForLessThanOrEqualsToOperator() {
    List<User> users = filterService.executeQuery(
      "{\"where\":{\"age\":{\"lte\":\"24\"}}}",
      User.class
    );

    assertThat(users).hasSize(3).contains(user1, user2, user3);
  }

  @Test
  void testFilterWithJson_ForLessThanOrEqualsToOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () ->
        filterService.executeQuery(
          "{\"where\":{\"age\":{\"lte\":\"24s\"}}}",
          User.class
        )
    );
  }

  @Test
  void testFilterWithJson_ForLikeOperator() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"permissions\":{\"like\":\"Update\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(2).contains(adminRole, userRole);
  }

  @Test
  void testFilterWithJson_ForNotLikeOperator() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"permissions\":{\"nlike\":\"Update\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(1);
    assertThat(tempRole.getId()).isEqualTo(roles.get(0).getId());
  }

  @Test
  void testFilterWithJson_FieldSelectionTest() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"permissions\":{\"nlike\":\"Update\"}},\"fields\":{\"name\":true}}",
      Role.class
    );

    assertThat(roles).hasSize(1);

    assertNull(roles.get(0).getId());
    assertNull(roles.get(0).getPermissions());
    assertThat(tempRole.getName()).isEqualTo(roles.get(0).getName());
  }

  @Test
  void testFilterWithJson_ForInOerator() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"id\":{\"in\": [\"" +
      tempRole.getId() +
      "\", \"" +
      adminRole.getId() +
      "\"]}}}",
      Role.class
    );

    assertThat(roles).hasSize(2).contains(tempRole, adminRole);
  }

  @Test
  void testFilterWithJson_ForNotInOerator() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"id\":{\"nin\": [\"" +
      tempRole.getId() +
      "\", \"" +
      adminRole.getId() +
      "\"]}}}",
      Role.class
    );

    assertThat(roles).hasSize(1).contains(userRole);
  }

  @Test
  void testFilterWithJson_FieldSelectionTestForMultipleRoles() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"permissions\":{\"like\":\"Update\"}},\"fields\":{\"name\":true}}",
      Role.class
    );

    assertThat(roles).hasSize(2);

    assertNull(roles.get(0).getId());
    assertNull(roles.get(0).getPermissions());
    assertNotNull(roles.get(0).getName());

    assertNull(roles.get(1).getId());
    assertNull(roles.get(1).getPermissions());
    assertNotNull(roles.get(1).getName());
  }

  @Test
  void testFilterWithJson_AndOperation() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"and\":{\"permissions\":{\"like\":\"Update\"},\"name\":\"Admin\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(1).contains(adminRole);
  }

  @Test
  void testFilterWithJson_OrInsideAndOperation() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"and\":{\"or\":{\"permissions\":{\"like\":\"Update\"},\"name\":\"Admin\"},\"permissions\":\"Get,Update,Find,Delete\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(1).contains(adminRole);
  }

  @Test
  void testFilterWithJson_AndInsideOrOperation() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"or\":{\"and\":{\"permissions\":{\"like\":\"Update\"},\"name\":\"Admin\"},\"permissions\":\"Get\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(2).contains(adminRole, tempRole);
  }

  @Test
  void testFilterWithJson_OrOperation() {
    List<Role> roles = filterService.executeQuery(
      "{\"where\":{\"or\":{\"permissions\":{\"like\":\"Update\"},\"name\":\"Temp\"}}}",
      Role.class
    );

    assertThat(roles).hasSize(3).contains(adminRole, userRole, tempRole);
  }

  @Test
  void testFilterWithJson_InvalidJson() {
    assertThrows(
      IllegalArgumentException.class,
      () ->
        filterService.executeQuery(
          "{\"where1\":{\"or\":{\"permissions\":{\"like\":\"Update\"},\"name\":\"Temp\"}}}",
          Role.class
        )
    );
  }
}
