package com.sourcefuse.jarc.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.core.test.models.Role;
import com.sourcefuse.jarc.core.test.repositories.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
class SoftDeleteTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  RoleRepository roleRepository;

  Role tempRole = new Role();
  Role userRole = new Role();
  Role adminRole = new Role();
  Example<Role> tempRoleExample;

  @BeforeEach
  void setUp() {
    TestConstants.clearTables(entityManager);
    TestConstants.setCurrentLoggedInUser();

    adminRole.setName("Admin");
    adminRole.setPermissions("Get,Update,Find,Delete");
    adminRole = this.roleRepository.save(adminRole);

    userRole.setName("User");
    userRole.setPermissions("Get,Update,Find");
    userRole = this.roleRepository.save(userRole);

    tempRole.setName("Temp");
    tempRole.setPermissions("Get");
    tempRole = this.roleRepository.save(tempRole);

    Role role = new Role();
    role.setId(tempRole.getId());
    tempRoleExample =
      Example.of(role, ExampleMatcher.matchingAll().withIgnoreCase());
  }

  @Test
  void testFindAll() {
    List<Role> result = this.roleRepository.findAllIncludeSoftDelete();

    assertThat(result).containsExactly(adminRole, tempRole, userRole);
  }

  @Test
  void testFindAllPageable() {
    Page<Role> result = this.roleRepository.findAll(PageRequest.of(0, 2));

    assertThat(result.getContent()).containsExactly(adminRole, userRole);
  }

  @Test
  void testFindAllIds() {
    List<Role> result =
      this.roleRepository.findAllById(Collections.singleton(tempRole.getId()));

    assertThat(result).containsExactly(tempRole);
  }

  @Test
  void testFindById() {
    Optional<Role> result = this.roleRepository.findById(tempRole.getId());

    assertThat(result).isPresent().contains(tempRole);
  }

  @Test
  void testSoftDeleteById() {
    this.roleRepository.deleteById(tempRole.getId());

    assertThat(this.roleRepository.findById(tempRole.getId())).isEmpty();
  }

  @Test
  void testSoftDelete() {
    this.roleRepository.delete(tempRole);

    assertThat(this.roleRepository.findById(tempRole.getId())).isEmpty();
  }

  @Test
  void testSoftDeleteAll() {
    this.roleRepository.deleteAll();

    assertThat(this.roleRepository.findAll()).isEmpty();
  }

  @Test
  void testCountActive() {
    long result = this.roleRepository.count();

    assertThat(result).isEqualTo(3);
  }

  @Test
  void testExistsActive() {
    boolean result = this.roleRepository.existsById(tempRole.getId());

    assertThat(result).isTrue();
  }

  /**
   * Test that a deleted entity is marked as deleted
   */
  @Test
  void shouldMarkDeletedEntityAsDeleted() {
    this.roleRepository.delete(tempRole);

    assertTrue(tempRole.isDeleted());
  }

  /**
   * Test that a deleted entity should have deleted by
   */
  @Test
  void deletedEntityShouldHaveDeletedBy() {
    this.roleRepository.delete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findByIdIncludeSoftDelete(tempRole.getId())
        .orElse(null);

    assertNotNull(roleAfterSoftDelete.getDeletedBy());
    assertEquals(TestConstants.mockUserId, roleAfterSoftDelete.getDeletedBy());
  }

  /**
   * Test that a deleted entity should have deleted by
   */
  @Test
  void deletedEntityShouldHaveDeletedOn() {
    this.roleRepository.delete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findByIdIncludeSoftDelete(tempRole.getId())
        .orElse(null);

    assertNotNull(roleAfterSoftDelete.getDeletedOn());
  }

  /**
   * Test that a deleted entity is not deleted permanently
   */
  @Test
  void shouldNotGetPermanentlyDeleted() {
    this.roleRepository.delete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findByIdIncludeSoftDelete(tempRole.getId())
        .orElse(null);

    assertNotNull(roleAfterSoftDelete);
    assertThat(roleAfterSoftDelete.isDeleted()).isTrue();
  }

  /**
   * Test that a deleted entity is not returned in findActiveById
   */
  @Test
  void entityReturnByFindActiveByIdShouldBeNull() {
    this.roleRepository.delete(tempRole);

    assertNull(this.roleRepository.findById(tempRole.getId()).orElse(null));
  }

  /**
   * Test that a deleted entity is not returned in findAllActive
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActive() {
    this.roleRepository.delete(tempRole);
    List<Role> result = this.roleRepository.findAll();

    assertThat(result).containsExactly(adminRole, userRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive Sort
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActiveSort() {
    this.roleRepository.delete(tempRole);
    List<Role> result =
      this.roleRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));

    assertThat(result).containsExactly(userRole, adminRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive Pageable
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActivePageable() {
    this.roleRepository.delete(userRole);
    Page<Role> result = this.roleRepository.findAll(PageRequest.of(0, 3));

    assertThat(result).containsExactly(adminRole, tempRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive by Id's
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActiveByIds() {
    this.roleRepository.delete(userRole);
    List<UUID> roleIds = new ArrayList<UUID>();
    roleIds.add(adminRole.getId());
    roleIds.add(tempRole.getId());
    roleIds.add(userRole.getId());
    List<Role> result = this.roleRepository.findAllById(roleIds);

    assertThat(result).containsExactly(adminRole, tempRole);
  }

  /**
   * Test that a deleted entity is not exists and active, existsActive should
   * return false
   */
  @Test
  void existsActiveShouldBeFalseForDeletedEntity() {
    this.roleRepository.delete(tempRole);
    boolean result = this.roleRepository.existsById(tempRole.getId());

    assertThat(result).isFalse();
  }

  /**
   * Test that a entity active count is different from entity total count
   */
  @Test
  void entityActiveCountAndEntityCountShouldBeDifferent() {
    this.roleRepository.delete(tempRole);
    long totalActiveEntities = this.roleRepository.count();
    long totalEntities = this.roleRepository.countIncludeSoftDelete();

    assertNotEquals(totalActiveEntities, totalEntities);
    assertEquals(2, totalActiveEntities);
    assertEquals(3, totalEntities);
  }

  /**
   * Test that a deleted entity can be restored
   */
  @Test
  void deletedEntityShouldBeRestorable() {
    this.roleRepository.delete(tempRole);

    tempRole.setDeleted(false);
    this.roleRepository.save(tempRole);

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isTrue();
  }

  @Test
  void testDeleteByIdHard() {
    this.roleRepository.deleteByIdHard(tempRole.getId());

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void testDeleteHard() {
    this.roleRepository.deleteHard(tempRole);

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void testDeleteAllByIdHard() {
    this.roleRepository.deleteAllByIdHard(
        Collections.singleton(tempRole.getId())
      );

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).containsExactly(adminRole, tempRole, userRole);
  }

  @Test
  void testFindAllPageable() {
    Page<Role> result = this.roleRepository.findAll(PageRequest.of(0, 2));

    assertThat(result.getContent()).containsExactly(adminRole, userRole);
  }

  @Test
  void testFindAllIds() {
    List<Role> result =
      this.roleRepository.findAllById(Collections.singleton(tempRole.getId()));

    assertThat(result).containsExactly(tempRole);
  }

  @Test
  void testFindById() {
    Optional<Role> result = this.roleRepository.findById(tempRole.getId());

    assertThat(result).isPresent().contains(tempRole);
  }

  @Test
  void testSoftDeleteById() {
    this.roleRepository.deleteById(tempRole.getId());

    assertThat(this.roleRepository.findById(tempRole.getId())).isEmpty();
  }

  @Test
  void testSoftDelete() {
    this.roleRepository.delete(tempRole);

    assertThat(this.roleRepository.findById(tempRole.getId())).isEmpty();
  }

  @Test
  void testSoftDeleteAll() {
    this.roleRepository.deleteAll();

    assertThat(this.roleRepository.findAll()).isEmpty();
  }

  @Test
  void testCountActive() {
    long result = this.roleRepository.count();

    assertThat(result).isEqualTo(3);
  }

  @Test
  void testExistsActive() {
    boolean result = this.roleRepository.existsById(tempRole.getId());

    assertThat(result).isTrue();
  }

  /**
   * Test that a deleted entity is marked as deleted
   */
  @Test
  void shouldMarkDeletedEntityAsDeleted() {
    this.roleRepository.delete(tempRole);

    assertTrue(tempRole.isDeleted());
  }

  /**
   * Test that a deleted entity should have deleted by
   */
  @Test
  void deletedEntityShouldHaveDeletedBy() {
    this.roleRepository.delete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findByIdIncludeSoftDelete(tempRole.getId())
        .orElse(null);

    assertNotNull(roleAfterSoftDelete.getDeletedBy());
    assertEquals(TestConstants.mockUserId, roleAfterSoftDelete.getDeletedBy());
  }

  /**
   * Test that a deleted entity should have deleted by
   */
  @Test
  void deletedEntityShouldHaveDeletedOn() {
    this.roleRepository.delete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findByIdIncludeSoftDelete(tempRole.getId())
        .orElse(null);

    assertNotNull(roleAfterSoftDelete.getDeletedOn());
  }

  /**
   * Test that a deleted entity is not deleted permanently
   */
  @Test
  void shouldNotGetPermanentlyDeleted() {
    this.roleRepository.delete(tempRole);

    Role roleAfterSoftDelete =
      this.roleRepository.findByIdIncludeSoftDelete(tempRole.getId())
        .orElse(null);

    assertNotNull(roleAfterSoftDelete);
    assertThat(roleAfterSoftDelete.isDeleted()).isTrue();
  }

  /**
   * Test that a deleted entity is not returned in findActiveById
   */
  @Test
  void entityReturnByFindActiveByIdShouldBeNull() {
    this.roleRepository.delete(tempRole);

    assertNull(this.roleRepository.findById(tempRole.getId()).orElse(null));
  }

  /**
   * Test that a deleted entity is not returned in findAllActive
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActive() {
    this.roleRepository.delete(tempRole);
    List<Role> result = this.roleRepository.findAll();

    assertThat(result).containsExactly(adminRole, userRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive Sort
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActiveSort() {
    this.roleRepository.delete(tempRole);
    List<Role> result =
      this.roleRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));

    assertThat(result).containsExactly(userRole, adminRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive Pageable
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActivePageable() {
    this.roleRepository.delete(userRole);
    Page<Role> result = this.roleRepository.findAll(PageRequest.of(0, 3));

    assertThat(result).containsExactly(adminRole, tempRole);
  }

  /**
   * Test that a deleted entity is not returned in findAllActive by Id's
   */
  @Test
  void deletedEntityShouldNotBePartOfFindAllActiveByIds() {
    this.roleRepository.delete(userRole);
    List<UUID> roleIds = new ArrayList<UUID>();
    roleIds.add(adminRole.getId());
    roleIds.add(tempRole.getId());
    roleIds.add(userRole.getId());
    List<Role> result = this.roleRepository.findAllById(roleIds);

    assertThat(result).containsExactly(adminRole, tempRole);
  }

  /**
   * Test that a deleted entity is not exists and active, existsActive should
   * return false
   */
  @Test
  void existsActiveShouldBeFalseForDeletedEntity() {
    this.roleRepository.delete(tempRole);
    boolean result = this.roleRepository.existsById(tempRole.getId());

    assertThat(result).isFalse();
  }

  /**
   * Test that a entity active count is different from entity total count
   */
  @Test
  void entityActiveCountAndEntityCountShouldBeDifferent() {
    this.roleRepository.delete(tempRole);
    long totalActiveEntities = this.roleRepository.count();
    long totalEntities = this.roleRepository.countIncludeSoftDelete();

    assertNotEquals(totalActiveEntities, totalEntities);
    assertEquals(2, totalActiveEntities);
    assertEquals(3, totalEntities);
  }

  /**
   * Test that a deleted entity can be restored
   */
  @Test
  void deletedEntityShouldBeRestorable() {
    this.roleRepository.delete(tempRole);

    tempRole.setDeleted(false);
    this.roleRepository.save(tempRole);

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isTrue();
  }

  @Test
  void testDeleteByIdHard() {
    this.roleRepository.deleteByIdHard(tempRole.getId());

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void testDeleteHard() {
    this.roleRepository.deleteHard(tempRole);

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void testDeleteAllByIdHard() {
    this.roleRepository.deleteAllByIdHard(
        Collections.singleton(tempRole.getId())
      );

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

  @Test
  void testDeleteAllByIdInBatchHard() {
    this.roleRepository.deleteAllByIdInBatchHard(
        Collections.singleton(tempRole.getId())
      );

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void testDeleteAllHardByEntities() {
    this.roleRepository.deleteAllHard(Collections.singleton(tempRole));

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void testDeleteAllInBatchHardByEntities() {
    this.roleRepository.deleteAllInBatchHard(Collections.singleton(tempRole));

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void testDeleteAllHard() {
    this.roleRepository.deleteAllHard();

    List<Role> roles = this.roleRepository.findAllIncludeSoftDelete();

    assertEquals(0, roles.size());
  }

    assertEquals(0, roles.size());
  }

    List<Role> roles = this.roleRepository.findAllIncludeSoftDelete();

    assertEquals(0, roles.size());
  }

    assertEquals(0, roles.size());
  }

    Role role =
      this.roleRepository.findByIdIncludeSoftDelete(tempRole.getId())
        .orElse(null);

    assertNotNull(role);
  }

  @Test
  void testExistsByIdIncludeSoftDelete() {
    this.roleRepository.deleteById(tempRole.getId());

    Boolean roleEsists =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertTrue(roleEsists);
  }

  @Test
  void testFindAllIncludeSoftDelete() {
    this.roleRepository.deleteById(tempRole.getId());

    List<Role> rolesIncludeDeleted =
      this.roleRepository.findAllIncludeSoftDelete();
    List<Role> roles = this.roleRepository.findAll();

    assertEquals(3, rolesIncludeDeleted.size());
    assertEquals(2, roles.size());
  }

    assertEquals(3, rolesIncludeDeleted.size());
    assertEquals(2, roles.size());
  }

    List<Role> roles =
      this.roleRepository.findAllByIdIncludeSoftDelete(
          Collections.singleton(tempRole.getId())
        );

    assertEquals(1, roles.size());
  }

    assertEquals(1, roles.size());
  }

    List<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(
          Sort.by(Sort.Direction.DESC, "name")
        );

    assertEquals(3, roles.size());
    assertThat(roles).contains(userRole, tempRole, adminRole);
  }

    assertEquals(3, roles.size());
    assertThat(roles).contains(userRole, tempRole, adminRole);
  }

    Page<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(PageRequest.of(0, 3));

    assertNotNull(roles.filter(ele -> ele.getName().equals("Admin")).toList());
    assertNotNull(roles.filter(ele -> ele.getName().equals("User")).toList());
    assertNotNull(roles.filter(ele -> ele.getName().equals("Temp")).toList());
  }

  @Test
  void testFindOneIncludeSoftDelete() {
    this.roleRepository.deleteById(tempRole.getId());
    Role role =
      this.roleRepository.findOneIncludeSoftDelete(
          Specification.where(getById(tempRole.getId()))
        )
        .orElse(null);
    assertNotNull(role);
  }

  @Test
  void findAllIncludeSoftDelete() {
    this.roleRepository.deleteById(tempRole.getId());

    List<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(
          Specification.where(getById(tempRole.getId()))
        );

    assertEquals(1, roles.size());
  }

    assertEquals(1, roles.size());
  }

  @Test
  void findAllIncludeSoftDeleteWithSpecificationAndPageable() {
    this.roleRepository.deleteById(tempRole.getId());
    Page<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(
          Specification.where(permissionLike("Get")),
          PageRequest.of(0, 3)
        );

    assertNotNull(roles.filter(ele -> ele.getName().equals("Admin")).toList());
    assertNotNull(roles.filter(ele -> ele.getName().equals("User")).toList());
    assertNotNull(roles.filter(ele -> ele.getName().equals("Temp")).toList());
  }

  @Test
  void findAllIncludeSoftDeleteWithSpecificationAndSort() {
    this.roleRepository.deleteById(userRole.getId());

    List<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(
          Specification.where(permissionLike("Update")),
          Sort.by(Sort.Direction.DESC, "name")
        );

    assertEquals(2, roles.size());
    assertThat(roles).contains(userRole, adminRole);
  }

  @Test
  void testFindOneIncludeSoftDeleteByExample() {
    this.roleRepository.deleteById(tempRole.getId());

    Role rolesExists =
      this.roleRepository.findOneIncludeSoftDelete(tempRoleExample)
        .orElse(null);

    assertNotNull(rolesExists);
  }

  @Test
  void testCountIncludeSoftDeleteByExample() {
    this.roleRepository.deleteById(tempRole.getId());

    Long roleCount =
      this.roleRepository.countIncludeSoftDelete(tempRoleExample);

    assertEquals(1, roleCount);
  }

  @Test
  void testExistsIncludeSoftDeleteByExample() {
    this.roleRepository.deleteById(tempRole.getId());

    boolean rolesExists =
      this.roleRepository.existsIncludeSoftDelete(tempRoleExample);

    assertTrue(rolesExists);
  }

  @Test
  void testExistsIncludeSoftDeleteBySpecification() {
    this.roleRepository.deleteById(tempRole.getId());

    boolean rolesExists =
      this.roleRepository.existsIncludeSoftDelete(
          Specification.where(getById(tempRole.getId()))
        );

    assertTrue(rolesExists);
  }

  @Test
  void testDeleteHardBySpecification() {
    Long deletedEntities =
      this.roleRepository.deleteHard(
          Specification.where(getById(tempRole.getId()))
        );

    assertEquals(1, deletedEntities);

    boolean result =
      this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

    assertThat(result).isFalse();
  }

  @Test
  void findAllIncludeSoftDeleteWithExample() {
    this.roleRepository.deleteById(tempRole.getId());

    List<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(tempRoleExample);

    assertEquals(1, roles.size());
  }

  @Test
  void findAllIncludeSoftDeleteWithExampleAndSort() {
    this.roleRepository.deleteById(tempRole.getId());

    List<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(
          tempRoleExample,
          Sort.by(Sort.Direction.DESC, "name")
        );

    assertEquals(1, roles.size());
  }

    Page<Role> roles =
      this.roleRepository.findAllIncludeSoftDelete(
          tempRoleExample,
          PageRequest.of(0, 1)
        );

    assertEquals(roles.toList().size(), 1);
    assertNotNull(roles.filter(ele -> ele.getName().equals("Temp")).toList());
  }

    assertEquals(1, roles.toList().size());
    assertNotNull(roles.filter(ele -> ele.getName().equals("Temp")).toList());
  }

    assertEquals(1, result.size());

    assertEquals(1, result.size());

  @Test
  void testFindByIncludeSoftDeleteWithSpecification() {
    List<Role> result =
      this.roleRepository.findByIncludeSoftDelete(
          permissionLike("Update"),
          query -> query.all()
        );

    assertEquals(2, result.size());
  }

    assertEquals(2, result.size());
  }

    Long roles = this.roleRepository.countIncludeSoftDelete();

    assertEquals(3, roles);
  }

    assertEquals(3, roles);
  }

  @Test
  void testCountIncludeSoftDeleteBySoecification() {
    this.roleRepository.deleteById(tempRole.getId());

    Long roles =
      this.roleRepository.countIncludeSoftDelete(
          Specification.where(getById(tempRole.getId()))
        );

    assertEquals(1, roles);
  }

		assertEquals(3, roles.size());
		assertThat(roles).contains(userRole, tempRole, adminRole);
	}

	@Test
	void testFindAllIncludeSoftDeleteWithPageable() {
		this.roleRepository.deleteById(tempRole.getId());

		Page<Role> roles = this.roleRepository.findAllIncludeSoftDelete(PageRequest.of(0, 3));

		assertNotNull(roles.filter(ele -> ele.getName().equals("Admin")).toList());
		assertNotNull(roles.filter(ele -> ele.getName().equals("User")).toList());
		assertNotNull(roles.filter(ele -> ele.getName().equals("Temp")).toList());
	}

	@Test
	void testFindOneIncludeSoftDelete() {
		this.roleRepository.deleteById(tempRole.getId());
		Role role = this.roleRepository.findOneIncludeSoftDelete(Specification.where(getById(tempRole.getId())))
				.orElse(null);
		assertNotNull(role);
	}

	@Test
	void findAllIncludeSoftDelete() {
		this.roleRepository.deleteById(tempRole.getId());

		List<Role> roles = this.roleRepository.findAllIncludeSoftDelete(Specification.where(getById(tempRole.getId())));

		assertEquals(1, roles.size());
	}

	@Test
	void findAllIncludeSoftDeleteWithSpecificationAndPageable() {
		this.roleRepository.deleteById(tempRole.getId());
		Page<Role> roles = this.roleRepository.findAllIncludeSoftDelete(Specification.where(permissionLike("Get")),
				PageRequest.of(0, 3));

		assertNotNull(roles.filter(ele -> ele.getName().equals("Admin")).toList());
		assertNotNull(roles.filter(ele -> ele.getName().equals("User")).toList());
		assertNotNull(roles.filter(ele -> ele.getName().equals("Temp")).toList());
	}

	@Test
	void findAllIncludeSoftDeleteWithSpecificationAndSort() {
		this.roleRepository.deleteById(userRole.getId());

		List<Role> roles = this.roleRepository.findAllIncludeSoftDelete(Specification.where(permissionLike("Update")),
				Sort.by(Sort.Direction.DESC, "name"));

		assertEquals(2, roles.size());
		assertThat(roles).contains(userRole, adminRole);
	}

	@Test
	void testFindOneIncludeSoftDeleteByExample() {
		this.roleRepository.deleteById(tempRole.getId());

		Role rolesExists = this.roleRepository.findOneIncludeSoftDelete(tempRoleExample).orElse(null);

		assertNotNull(rolesExists);
	}

	@Test
	void testCountIncludeSoftDeleteByExample() {
		this.roleRepository.deleteById(tempRole.getId());

		Long roleCount = this.roleRepository.countIncludeSoftDelete(tempRoleExample);

		assertEquals(1, roleCount);
	}

	@Test
	void testExistsIncludeSoftDeleteByExample() {
		this.roleRepository.deleteById(tempRole.getId());

		boolean rolesExists = this.roleRepository.existsIncludeSoftDelete(tempRoleExample);

		assertTrue(rolesExists);
	}

	@Test
	void testExistsIncludeSoftDeleteBySpecification() {
		this.roleRepository.deleteById(tempRole.getId());

		boolean rolesExists = this.roleRepository
				.existsIncludeSoftDelete(Specification.where(getById(tempRole.getId())));

		assertTrue(rolesExists);
	}

	@Test
	void testDeleteHardBySpecification() {
		Long deletedEntities = this.roleRepository.deleteHard(Specification.where(getById(tempRole.getId())));

		assertEquals(deletedEntities, 1);

		boolean result = this.roleRepository.existsByIdIncludeSoftDelete(tempRole.getId());

		assertThat(result).isFalse();
	}

	@Test
	void findAllIncludeSoftDeleteWithExample() {
		this.roleRepository.deleteById(tempRole.getId());

		List<Role> roles = this.roleRepository.findAllIncludeSoftDelete(tempRoleExample);

		assertEquals(1, roles.size());
	}

	@Test
	void findAllIncludeSoftDeleteWithExampleAndSort() {
		this.roleRepository.deleteById(tempRole.getId());

		List<Role> roles = this.roleRepository.findAllIncludeSoftDelete(tempRoleExample,
				Sort.by(Sort.Direction.DESC, "name"));

		assertEquals(1, roles.size());
	}

	@Test
	void findAllIncludeSoftDeleteWithExampleAndPageable() {
		this.roleRepository.deleteById(tempRole.getId());

		Page<Role> roles = this.roleRepository.findAllIncludeSoftDelete(tempRoleExample, PageRequest.of(0, 1));

		assertEquals(roles.toList().size(), 1);
		assertNotNull(roles.filter(ele -> ele.getName().equals("Temp")).toList());
	}

	@Test
	void testFindByIncludeSoftDeleteWithExample() {
		List<Role> result = this.roleRepository.findByIncludeSoftDelete(tempRoleExample, query -> query.all());

		assertEquals(1, result.size());

		assertThat(result).contains(tempRole);
	}

	@Test
	void testFindByIncludeSoftDeleteWithSpecification() {
		List<Role> result = this.roleRepository.findByIncludeSoftDelete(permissionLike("Update"), query -> query.all());

		assertEquals(2, result.size());
	}

	@Test
	void testCountIncludeSoftDelete() {
		this.roleRepository.deleteById(tempRole.getId());

		Long roles = this.roleRepository.countIncludeSoftDelete();

		assertEquals(3, roles);
	}

	@Test
	void testCountIncludeSoftDeleteBySoecification() {
		this.roleRepository.deleteById(tempRole.getId());

		Long roles = this.roleRepository.countIncludeSoftDelete(Specification.where(getById(tempRole.getId())));

		assertEquals(roles, 1);
	}

	private Specification<Role> getById(UUID id) {
		return (root, query, builder) -> builder.equal(root.get("id"), id);
	}

	private Specification<Role> permissionLike(String permission) {
		return (root, query, builder) -> builder.like(root.get("permissions"), "%" + permission + "%");
	}
}
