package com.rbouaro.authorization.controller;


import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {
	@GetMapping("/hello")
	public String hello() {
		return "Hello from Resource Controller!";
	}

	@GetMapping("/goodbye")
	public String goodbye() {
		return "Goodbye from Resource Controller!";
	}

	@GetMapping("/management/hello")
	public String helloManagement() {
		return "Hello from Management Resource Controller!";
	}

	@PostMapping("/management/hello")
	@PreAuthorize("hasAuthority('write') and @securityGuard.validateHelloMessage(#message)")
	public String helloManagementPost(@RequestBody String message) {
		return "Hello from Management Resource Controller!" + message;
	}

	@GetMapping("/management/iam/{id}")
	@PostAuthorize("returnObject.contains(#id)")
	public String helloManagementPath(@PathVariable String id) {

		return "Hello from Management Resource Controller! 3b4ac1db-3d02-42d9-b9c8-253b352d6cd5";
	}

	@GetMapping("/management/tasks")
	@PreFilter("filterObject.startsWith('Task')")
	public List<String> getTasks(@RequestBody List<String> tasks) {

		return List.of("Task 1", "Task 2", "Task 3");
	}


}