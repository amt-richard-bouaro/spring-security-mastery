package com.rbouaro.customauthentication.contoller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

     @GetMapping
     public String getResource() {
         return "This is an example endpoint";
     }
}