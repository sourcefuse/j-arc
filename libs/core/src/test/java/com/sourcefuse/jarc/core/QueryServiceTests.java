package com.sourcefuse.jarc.core;

import static org.assertj.core.api.Assertions.assertThat;
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
public class QueryServiceTests {

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

  @Test
  void testFIlterForEqualsOperator() {
    fieldsOperation.put("eq", "User");
    filterWhere.put("name", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles.size()).isEqualTo(1);

    assertThat(userRole.getId()).isEqualTo(roles.get(0).getId());
  }

  @Test
  void testFIlterForNotEqualsOperator() {
    fieldsOperation.put("ne", "User");
    filterWhere.put("name", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles.size()).isEqualTo(2);

    assertThat(roles).contains(adminRole, tempRole);
  }

  @Test
  void testFIlterForGreaterThanOperator() {
    fieldsOperation.put("gt", "20");
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    List<User> users = filterService.executeQuery(filter, User.class);

    assertThat(users.size()).isEqualTo(2);

    assertThat(users).contains(user2, user3);
  }

  @Test
  void testFIlterForGreaterThanOperatorWithString() {
    fieldsOperation.put("gt", "24s");
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    assertThrows(
      NumberFormatException.class,
      () -> {
        filterService.executeQuery(filter, User.class);
      }
    );
  }

  @Test
  void testFIlterForGreaterThanOrEqulsToOperator() {
    fieldsOperation.put("gte", "20");
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    List<User> users = filterService.executeQuery(filter, User.class);

    assertThat(users.size()).isEqualTo(3);

    assertThat(users).contains(user1, user2, user3);
  }

  @Test
  void testFIlterForGreaterThanOrEqulsToOperatorWithString() {
    fieldsOperation.put("gte", "24s");
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    assertThrows(
      NumberFormatException.class,
      () -> {
        filterService.executeQuery(filter, User.class);
      }
    );
  }

  @Test
  void testFIlterForLessThanOperator() {
    fieldsOperation.put("lt", 24);
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    List<User> users = filterService.executeQuery(filter, User.class);

    assertThat(users.size()).isEqualTo(2);

    assertThat(users).contains(user1, user2);
  }

  @Test
  void testFIlterForLessThanOperatorWithString() {
    fieldsOperation.put("lt", "24s");
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    assertThrows(
      NumberFormatException.class,
      () -> {
        filterService.executeQuery(filter, User.class);
      }
    );
  }

  @Test
  void testFIlterForLessThanOrEqualsToOperator() {
    fieldsOperation.put("lte", "24");
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    List<User> users = filterService.executeQuery(filter, User.class);

    assertThat(users.size()).isEqualTo(3);

    assertThat(users).contains(user1, user2, user3);
  }

  @Test
  void testFIlterForLessThanOrEqualsToOperatorWithString() {
    fieldsOperation.put("lte", "24s");
    filterWhere.put("age", fieldsOperation);
    filter.setWhere(filterWhere);

    assertThrows(
      NumberFormatException.class,
      () -> {
        filterService.executeQuery(filter, User.class);
      }
    );
  }

  @Test
  void testFIlterForLikeOperator() {
    fieldsOperation.put("like", "Update");
    filterWhere.put("permissions", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles.size()).isEqualTo(2);

    assertThat(roles).contains(adminRole, userRole);
  }

  @Test
  void testFIlterForNotLikeOperator() {
    fieldsOperation.put("notlike", "Update");
    filterWhere.put("permissions", fieldsOperation);
    filter.setWhere(filterWhere);

    List<Role> roles = filterService.executeQuery(filter, Role.class);

    assertThat(roles.size()).isEqualTo(1);

    assertThat(tempRole.getId()).isEqualTo(roles.get(0).getId());
  }
}
