package com.sourcefuse.jarc.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.softdelete.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.core.test.models.Role;
import com.sourcefuse.jarc.core.test.repositories.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
public class SoftDeleteTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  RoleRepository roleRepository;

  Role tempRole = new Role();
  Role userRole = new Role();
  Role adminRole = new Role();

  @BeforeEach
  void setUp() {
    TestConstants.clearTables(entityManager);
    TestConstants.setCurrentLoggedInUser();

    adminRole.setName("Admin");
    adminRole.setPermissons("*");
    adminRole = this.roleRepository.save(adminRole);

    userRole.setName("User");
    userRole.setPermissons("Get,Update,Find");
    userRole = this.roleRepository.save(userRole);

    tempRole.setName("Temp");
    tempRole.setPermissons("Get");
    tempRole = this.roleRepository.save(tempRole);
  }

  @AfterEach
  public void waitAfterTest() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(250);
  }

  @Test
  void testFindAllActive() {
    List<Role> result = this.roleRepository.findAllActive();

    assertThat(result).containsExactly(adminRole, userRole, tempRole);
  }

  @Test
  void testFindAllActiveSort() {
    List<Role> result =
      this.roleRepository.findAllActive(Sort.by(Sort.Direction.ASC, "name"));

    assertThat(result).containsExactly(adminRole, tempRole, userRole);
  }

  @Test
  void testFindAllActivePageable() {
    Page<Role> result = this.roleRepository.findAllActive(PageRequest.of(0, 2));

    assertThat(result.getContent()).containsExactly(adminRole, userRole);
  }

  @Test
  void testFindAllActiveIds() {
    List<Role> result =
      this.roleRepository.findAllActive(
          Collections.singleton(tempRole.getId())
        );

    assertThat(result).containsExactly(tempRole);
  }

  @Test
  void testFindActiveById() {
    Optional<Role> result =
      this.roleRepository.findActiveById(tempRole.getId());

    assertThat(result).isPresent().contains(tempRole);
  }

  @Test
  void testSoftDeleteById() {
    this.roleRepository.softDeleteById(tempRole.getId());

    assertThat(this.roleRepository.findActiveById(tempRole.getId())).isEmpty();
  }

  @Test
  void testSoftDelete() {
    this.roleRepository.softDelete(tempRole);

    assertThat(this.roleRepository.findActiveById(tempRole.getId())).isEmpty();
  }

  @Test
  void testSoftDeleteAll() {
    this.roleRepository.softDeleteAll();

    assertThat(this.roleRepository.findAllActive()).isEmpty();
  }

  @Test
  void testCountActive() {
    long result = this.roleRepository.countActive();

    assertThat(result).isEqualTo(3);
  }

  @Test
  void testExistsActive() {
    boolean result = this.roleRepository.existsActive(tempRole.getId());

    assertThat(result).isTrue();
  }

  /**
   * Test that a deleted entity is marked as deleted
   */
  @Test
  void shouldMarkDeletedEntityAsDeleted() {
    this.roleRepository.softDelete(tempRole);

    assertTrue(tempRole.isDeleted());
  }

  /**
   * Test that a deleted entity should have deleted by
   */
  @Test
  void deletedEntityShouldHaveDeletedBy() {
    this.roleRepository.softDelete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findById(tempRole.getId()).orElse(null);

    assertNotNull(roleAfterSoftDelete.getDeletedBy());
    assertEquals(TestConstants.mockUserId, roleAfterSoftDelete.getDeletedBy());
  }

  /**
   * Test that a deleted entity should have deleted by
   */
  @Test
  void deletedEntityShouldHaveDeletedOn() {
    this.roleRepository.softDelete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findById(tempRole.getId()).orElse(null);

    assertNotNull(roleAfterSoftDelete.getDeletedOn());
  }

  /**
   * Test that a deleted entity is not deleted permanently
   */
  @Test
  void shouldNotGetPermanentlyDeleted() {
    this.roleRepository.softDelete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findById(tempRole.getId()).orElse(null);

    assertNotNull(roleAfterSoftDelete);
    assertThat(roleAfterSoftDelete.isDeleted()).isTrue();
  }

  /**
   * Test that a deleted entity is not returned in findActiveById
   */
  @Test
  void entityReturnByFindActiveByIdShouldBeNull() {
    this.roleRepository.softDelete(tempRole);

    assertNull(
      this.roleRepository.findActiveById(tempRole.getId()).orElse(null)
    );
  }

  /**
   * Test that a deleted entity is not returned in findAllActive
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActive() {
    this.roleRepository.softDelete(tempRole);
    List<Role> result = this.roleRepository.findAllActive();

    assertThat(result).containsExactly(adminRole, userRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive Sort
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActiveSort() {
    this.roleRepository.softDelete(tempRole);
    List<Role> result =
      this.roleRepository.findAllActive(Sort.by(Sort.Direction.DESC, "name"));

    assertThat(result).containsExactly(userRole, adminRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive Pageable
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActivePageable() {
    this.roleRepository.softDelete(userRole);
    Page<Role> result = this.roleRepository.findAllActive(PageRequest.of(0, 3));

    assertThat(result).containsExactly(adminRole, tempRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive by Id's
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActiveByIds() {
    this.roleRepository.softDelete(userRole);
    List<UUID> roleIds = new ArrayList<UUID>();
    roleIds.add(adminRole.getId());
    roleIds.add(tempRole.getId());
    roleIds.add(userRole.getId());
    List<Role> result = this.roleRepository.findAllActive(roleIds);

    assertThat(result).containsExactly(adminRole, tempRole);
  }

  /**
   * Test that a deleted entity is not exists and active, existsActive should
   * return false
   */
  @Test
  void existsActiveShouldBeFalseForDeletedEntity() {
    this.roleRepository.softDelete(tempRole);
    boolean result = this.roleRepository.existsActive(tempRole.getId());

    assertThat(result).isFalse();
  }

  /**
   * Test that a entity active count is different from entity total count
   */
  @Test
  void entityActiveCountAndEntityCountShouldBeDifferent() {
    this.roleRepository.softDelete(tempRole);
    long totalActiveEntities = this.roleRepository.countActive();
    long totalEntities = this.roleRepository.count();

    assertNotEquals(totalActiveEntities, totalEntities);
    assertEquals(totalActiveEntities, 2);
    assertEquals(totalEntities, 3);
  }

  /**
   * Test that a deleted entity can be restored
   */
  @Test
  void deletedEntityShouldBeRestorable() {
    this.roleRepository.softDelete(tempRole);

    tempRole.setDeleted(false);
    this.roleRepository.save(tempRole);

    boolean result = this.roleRepository.existsActive(tempRole.getId());

    assertThat(result).isTrue();
  }
}
