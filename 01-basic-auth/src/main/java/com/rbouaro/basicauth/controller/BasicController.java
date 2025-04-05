package com.rbouaro.basicauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BasicController {

    @RequestMapping("/admin/resources")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminOnlyResources() {
        return "Admin only resources";
    }

    @RequestMapping("/user/resources")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String otherResources() {
        return "Other resources";
    }
}