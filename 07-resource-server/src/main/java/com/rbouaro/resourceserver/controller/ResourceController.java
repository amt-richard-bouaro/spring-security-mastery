package com.rbouaro.resourceserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {
	@GetMapping
	@PreAuthorize("hasRole('ADMIN') and hasAuthority('read')")
	public String getResource(
			Authentication authentication
	) {
		return "Hello from Resource Server!";
	}

	@GetMapping("/admin")
	public String getAdminResource() {
		return "Hello from Admin Resource!";
	}

	@GetMapping("/user")
	public String getUserResource() {
		return "Hello from User Resource!";
	}
	@PostMapping("/admin")
	public String postAdminResource(@RequestBody String data) {
		return "Hello from Admin Resource!";
	}



}