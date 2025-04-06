package com.rbouaro.basicauthwithdb.controller;

import com.rbouaro.basicauthwithdb.adapter.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    @GetMapping
    public String getAllResources() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        StringBuilder authorities = new StringBuilder();

        securityUser.getAuthorities().forEach(authority -> {
            authorities.append(authority.getAuthority()).append("\n");
        });

        return """
                Welcome, %s
                You have access to all resources.
                
                Your Authorities are:
                %s
                
                """.formatted(securityUser.getUsername(), authorities);
    }
}