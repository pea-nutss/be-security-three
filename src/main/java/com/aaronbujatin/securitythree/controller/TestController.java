package com.aaronbujatin.securitythree.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@Slf4j
public class TestController {

    @GetMapping("/public")
    public String account(Authentication authentication){
        return "This is the public endpoint";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userEndpoint(Authentication authentication){
        log.info("Authentication Object : " + authentication.toString());
        return "This is role_user endpoint ";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminEndpoint(Authentication authentication){
        log.info("Authentication Object : " + authentication.toString());
        return "This is role_admin endpoint";
    }


}
