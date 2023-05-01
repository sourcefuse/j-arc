package com.sourcefuse.example.authenticationaudit.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.example.authenticationaudit.models.Todo;
import com.sourcefuse.example.authenticationaudit.repositories.TodoRepository;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

	@Autowired
	TodoRepository todoRepository;

	@GetMapping("/save")
	@PreAuthorize("isAuthenticated()")
	public String saveTodo() {
		System.out.println("started to Save the Todo");
		Todo todo = new Todo();
		todo.setTaskName("Task Todo" + new Date().toString());
		todo.setIsDone(false);
		this.todoRepository.save(todo);
		System.out.println("Saved the user");
		return "Authorized to access!";
	}

	@GetMapping("/update")
	@PreAuthorize("isAuthenticated()")
	public String updateTenant() {
		System.out.println("started to Update the Todo");
		Todo todo = this.todoRepository.findAll().iterator().next();
		if (todo != null) {
			todo.setTaskName("Task Todo updated" + new Date().toString());
			todo.setIsDone(true);
			this.todoRepository.save(todo);
		}
		System.out.println("updated the user");
		return "Todo Updated Successfully!";
	}

	@GetMapping("/save-all")
	@PreAuthorize("isAuthenticated()")
	public String saveAllTenant() {
		System.out.println("started to Update the Todo");
		List<Todo> todos = new ArrayList<Todo>();

		Todo todo = new Todo();
		todo.setTaskName("Task One Todo" + new Date().toString());
		todo.setIsDone(false);
		todos.add(todo);

		Todo todo1 = new Todo();
		todo1.setTaskName("Task Two Todo" + new Date().toString());
		todo1.setIsDone(false);
		todos.add(todo1);

		this.todoRepository.saveAll(todos);
		return "Todos are saved successfully";
	}

	@GetMapping("/delete")
	@PreAuthorize("isAuthenticated()")
	public String deleteTodo() {
		try {
			System.out.println("started to Save the Todo");
			Todo todo = this.todoRepository.findAllActive().iterator().next();
			this.todoRepository.softDeleteById(todo.getId());
//			this.todoRepository.deleteById(todo.getId());
			System.out.println("Saved the user");
			return "Authorized to access!";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@GetMapping("")
	public String getAllTenant() {
		System.out.println("started to Update the Todo");
		List<String> todos = new ArrayList<String>();
		this.todoRepository.findAll().forEach(todo -> todos.add(todo.toString()));

		return todos.toString();
	}

}
