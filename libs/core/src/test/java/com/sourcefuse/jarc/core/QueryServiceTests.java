package com.sourcefuse.jarc.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.models.filters.Filter;
import com.sourcefuse.jarc.core.models.filters.IncludeRelation;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.core.services.QueryService;
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
  Map<String, Object> filterWhere = new HashMap<String, Object>();
  Map<String, Object> fieldsOperation = new HashMap<String, Object>();
  List<IncludeRelation> listInclude = new ArrayList<>();

  @BeforeEach
  void setUp() {
    TestConstants.clearTables(entityManager);
    TestConstants.setCurrentLoggedInUser();
    this.setUpRole();

    filter = new Filter();
    filterWhere = new HashMap<String, Object>();
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
    user1.setRole(adminRole);
    user1.setLastName("Admin");
    user1.setAge(20);
    user1 = this.userRepository.save(user1);

    user2.setFirstName("User Two");
    user2.setRole(tempRole);
    user2.setLastName("Temp");
    user2.setAge(22);
    user2 = this.userRepository.save(user2);

    user3.setFirstName("User Three");
    user3.setRole(userRole);
    user3.setLastName("User");
    user3.setAge(24);
    user3 = this.userRepository.save(user3);
  }

  private List<User> filterWithOperatorAndAge(String operator, Object age) {
    fieldsOperation.put(operator, age);
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);
    return filterService.executeQuery(filter, User.class);
  }

  @Test
  void testFilterForEqualsOperator() {
    fieldsOperation.put("eq", "User");
    filterWhere.put("name", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1);

    assertThat(userRole.getId()).isEqualTo(roles.get(0).getId());
  }

  @Test
  void testFilterForNotEqualsOperator() {
    fieldsOperation.put("ne", "User");
    filterWhere.put("name", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2);

    assertThat(roles).contains(adminRole, tempRole);
  }

  @Test
  void testFilterForGreaterThanOperator() {
    List<User> users = this.filterWithOperatorAndAge("gt", "20");
    assertThat(users).hasSize(2);
    assertThat(users).contains(user2, user3);
  }

  @Test
  void testFilterForGreaterThanOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> {
        this.filterWithOperatorAndAge("gt", "24s");
      }
    );
  }

  @Test
  void testFilterForGreaterThanOrEqulsToOperator() {
    List<User> users = this.filterWithOperatorAndAge("gte", "20");

    assertThat(users).hasSize(3);
    assertThat(users).contains(user1, user2, user3);
  }

  @Test
  void testFilterForGreaterThanOrEqulsToOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> {
        this.filterWithOperatorAndAge("gte", "24s");
      }
    );
  }

  @Test
  void testFilterForLessThanOperator() {
    List<User> users = this.filterWithOperatorAndAge("lt", 24);

    assertThat(users).hasSize(2);
    assertThat(users).contains(user1, user2);
  }

  @Test
  void testFilterForLessThanOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> {
        this.filterWithOperatorAndAge("lt", "24s");
      }
    );
  }

  @Test
  void testFilterForLessThanOrEqualsToOperator() {
    List<User> users = this.filterWithOperatorAndAge("lte", "24");

    assertThat(users).hasSize(3);
    assertThat(users).contains(user1, user2, user3);
  }

  @Test
  void testFilterForLessThanOrEqualsToOperatorWithString() {
    assertThrows(
      NumberFormatException.class,
      () -> {
        this.filterWithOperatorAndAge("lte", "24s");
      }
    );
  }

  @Test
  void testFilterForLikeOperator() {
    fieldsOperation.put("like", "Update");
    filterWhere.put("permissions", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2);
    assertThat(roles).contains(adminRole, userRole);
  }

  @Test
  void testFilterForNotLikeOperator() {
    fieldsOperation.put("notlike", "Update");
    filterWhere.put("permissions", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1);
    assertThat(tempRole.getId()).isEqualTo(roles.get(0).getId());
  }

  @Test
  void testFilterFieldSelectionTest() {
    Map<String, Boolean> fields = new HashMap<String, Boolean>();
    fields.put("name", true);
    fieldsOperation.put("notlike", "Update");
    filterWhere.put("permissions", fieldsOperation);
    filter.setWhere(filterWhere);
    filter.setFields(fields);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(1);

    assertNull(roles.get(0).getId());
    assertNull(roles.get(0).getPermissions());
    assertThat(tempRole.getName()).isEqualTo(roles.get(0).getName());
  }

  @Test
  void testFilterFieldSelectionTestForMultipleRoles() {
    Map<String, Boolean> fields = new HashMap<String, Boolean>();
    fields.put("name", true);
    fieldsOperation.put("like", "Update");
    filterWhere.put("permissions", fieldsOperation);
    filter.setWhere(filterWhere);
    filter.setFields(fields);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles).hasSize(2);

    assertNull(roles.get(0).getId());
    assertNull(roles.get(0).getPermissions());
    assertNotNull(roles.get(0).getName());

    assertNull(roles.get(1).getId());
    assertNull(roles.get(1).getPermissions());
    assertNotNull(roles.get(1).getName());
  }
}
