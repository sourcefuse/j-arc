package com.sourcefuse.jarc.services.featuretoggleservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;
import com.sourcefuse.jarc.services.featuretoggleservice.enums.Features;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/todo")
public class DemoController {

	@FeatureToggle(value = Features.MAKE_JUICE)
	@GetMapping("/hello")
	@PreAuthorize("isAuthenticated()")
	public String sayHello() {
		CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return "hello";
	}

//	@PostMapping("/add")
//	@PreAuthorize("isAuthenticated()")
//	// @FeatureToggle(value = Features.MAKE_COFFEE)
//	public Todo addTodo(@RequestBody Todo todo) {
//		System.out.println("in here");
////		List<Todo> todoList = new ArrayList<>();
////		todoList.add(todo);
//		this.todoRepo.save(todo);
//		return null;
//
//	}
//
//	@PostMapping("/add")
//	@PreAuthorize("isAuthenticated()")
//	// @FeatureToggle(value = Features.MAKE_TEA)
//	public Todo add(@RequestBody Todo todo) {
//		return ((CrudRepository<Todo, UUID>) this.todoRepo).save(todo);
////		CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////		return user;
//	}

}
