//package com.sourcefuse.jarc.services.auditservice.controllers;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//import java.util.ArrayList;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.sourcefuse.jarc.services.auditservice.models.User;
//
//import lombok.extern.slf4j.Slf4j;
//
//import com.sourcefuse.jarc.services.auditservice.audit.repositories.UserRepository;
//
//@RestController
//@RequestMapping("/api/user")
//@Slf4j
//public class UserController {
//
//	@Autowired
//	UserRepository userRepository;
//
//	@GetMapping("/save")
//	public String saveUser() {
//		log.info("::::::: {} :::::: ", Thread.currentThread().getName());
//		log.info("started to Save the user");
//		User user = new User();
//		user.setFirstName("Harshad" + new Date().getTime());
//		user.setLastName("Kadam");
//		this.userRepository.save(user);
//		log.info("Saved the user");
//		return user.toString();
//	}
//
//	@GetMapping("/save-and-flush")
//	public String saveAndFlushUser() {
//		log.info("started to Save the user");
//		User user = new User();
//		user.setFirstName("Harshad" + new Date().getTime());
//		user.setLastName("Kadam");
//		this.userRepository.save(user);
//		log.info("Saved the user");
//		return "Authorized to access!";
//	}
//
//	@GetMapping("/update")
//	public String update() {
//		log.info("started to Save the user");
//		User user = new User();
//		user.setId(UUID.randomUUID());
//		user.setFirstName("Harshad" + new Date().getTime() + "Updated");
//		user.setLastName("Kadam ");
//		this.userRepository.save(user);
//		log.info("Saved the user");
//		return "Authorized to access!";
//	}
//
//
//	@GetMapping("/delete")
//	public String delete() {
//		log.info("started to Save the user");
//		User user = this.userRepository.findAll().get(0);
//		this.userRepository.delete(user);
//		return "Authorized to access!";
//	}
//
//	@GetMapping("/delete-by-id")
//	public String deleteById() {
//		log.info("started to Save the user");
//		User user = this.userRepository.findAll().get(0);
//		this.userRepository.deleteById(user.getId());
//		log.info("Saved the user");
//		return "Authorized to access!";
//	}
//
//
//	@GetMapping("/delete-all")
//	public String deleteAll() {
//		log.info("started to Save the user");
//		this.userRepository.deleteAll();
//		return "Authorized to access!";
//	}
//	
//	@GetMapping("/save-all")
//	public String saveAllUser() {
//		log.info("started to Save the user");
//		List<User> users = new ArrayList<>();
//		User user = new User();
//		user.setFirstName("Team" + new Date().getTime());
//		user.setLastName("Titan");
//		users.add(user);
//		User user1 = new User();
//		user1.setFirstName("Avengers" + new Date().getTime());
//		user1.setLastName("Assemble");
//		users.add(user1);
//		List<User> created = this.userRepository.saveAll(users);
//		log.info("Saved  all the user");
//		return created.toString();
//	}
//}
