package com.sourcefuse.jarc.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.core.services.Filter;
import com.sourcefuse.jarc.core.services.IncludeRelation;
import com.sourcefuse.jarc.core.services.QueryService;
import com.sourcefuse.jarc.core.test.models.Role;
import com.sourcefuse.jarc.core.test.models.User;
import com.sourcefuse.jarc.core.test.repositories.RoleRepository;
import com.sourcefuse.jarc.core.test.repositories.UserRepository;

@SpringBootTest
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
public class FilterServiceTests {

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

	@BeforeEach
	void setUp() {
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

		user1.setFirstName("User One");
		user1.setRole(adminRole);
		user1.setLastName("Admin");
		user1 = this.userRepository.save(user1);

		user2.setFirstName("User Two");
		user2.setRole(tempRole);
		user2.setLastName("Temp");
		user2 = this.userRepository.save(user2);

		user3.setFirstName("User Three");
		user3.setRole(tempRole);
		user3.setLastName("User");
		user3 = this.userRepository.save(user3);

	}

	@Test
	void filterUser() {
		Filter filter = new Filter();
		Filter relationFilter = new Filter();
		IncludeRelation include = new IncludeRelation();
//		Map<String, Object> relationFilterWhere = new HashMap<String, Object>();
		Map<String, Object> filterWhere = new HashMap<String, Object>();
		filterWhere.put("name", "Temp");
//		relationFilterWhere.put("firstName", "User Three");
		include.setRelation("users");
		include.setFilter(relationFilter);
//		relationFilter.setWhere(relationFilterWhere);
		filter.setWhere(filterWhere);
		List<IncludeRelation> listInclude = new ArrayList();
		listInclude.add(include);
		filter.setInclude(listInclude);

		List<Role> roles = filterService.executeQuery(filter, Role.class);
		for (User user : roles.get(0).getUsers()) {
			System.out.println(user.toString());
		}
		assertTrue(true);
	}
}
