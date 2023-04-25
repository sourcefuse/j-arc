//package com.sourcefuse.jarc.services.auditservice.controllers;
//
//import java.util.Date;
//import java.util.List;
//import java.util.ArrayList;
//
//import java.util.concurrent.CompletableFuture;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.sourcefuse.jarc.services.auditservice.audit.mixin.MyService;
//import com.sourcefuse.jarc.services.auditservice.models.Role;
//import com.sourcefuse.jarc.services.auditservice.repositories.RoleRepository;
//
//@RestController
//@RequestMapping("/api/role")
//public class RoleController {
//
//	@Autowired
//	RoleRepository roleRepository;
//
//	@GetMapping("/save")
//	public String saveRole() {
//		System.out.println("started to Save the role");
//		Role role = new Role();
//		role.setName("Harshad" + new Date().getTime());
//		role.setPermissons("Kadam");
//		this.roleRepository.save(role);
//		System.out.println("Saved the user");
//		return "Authorized to access!";
//	}
//
//	@GetMapping("/async")
//	public String saveR1ole() {
//		System.out.println("Method thread: " + Thread.currentThread().getName());
//
//		CompletableFuture<String> futureResult = MyService.doSomethingAsync("hello");
//
//		futureResult.thenRun(() -> {
//			System.out.println("Async method executed on thread: " + Thread.currentThread().getName());
//		});
//		return "Authorized to access!";
//	}
//
//	@GetMapping("/update")
//	public String updateTenant() {
//		System.out.println("started to Update the Role");
//		Role role = this.roleRepository.findAll().get(0);
//		if (role != null) {
//			role.setName("Updated Name" + new Date().getTime());
//			role.setPermissons("Role");
//			this.roleRepository.save(role);
//		}
//		System.out.println("updated the user");
//		return "Authorized to access!";
//	}
//
//	@GetMapping("/save-all")
//	public String saveAllTenant() {
//		System.out.println("started to Update the Role");
//		List<Role> roles = new ArrayList<Role>();
//
//		Role role = new Role();
//		role.setName("New Name" + new Date().getTime());
//		role.setPermissons("2");
//		roles.add(role);
//		
//		Role role1 = new Role();
//		role1.setName("New Name" + new Date().getTime());
//		role1.setPermissons("1");
//		roles.add(role1);
//		
//		this.roleRepository.saveAll(roles);
//		System.out.println("updated the user");
//		return "Authorized to access!";
//	}
//
//}
