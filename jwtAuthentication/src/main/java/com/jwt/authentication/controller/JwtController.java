package com.jwt.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.authentication.entity.User;
import com.jwt.authentication.request.TokenRefreshRequest;
import com.jwt.authentication.response.JwtResponse;
import com.jwt.authentication.response.TokenRefreshResponse;
import com.jwt.authentication.service.impl.JwtService;

@RestController
public class JwtController {

    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/generatetoken")
    public JwtResponse createsigninToken(@RequestBody User user) throws Exception {
        return jwtService.signinToken(user);
    }
    
    @GetMapping("/refreshtoken")
    public TokenRefreshResponse geRefreshResponse(@RequestBody TokenRefreshRequest request) {
    	return jwtService.getNewAccessToken(request);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String forAdmin() {
    	return "Hello Admin";
    }
    
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/user")
    public String forUser() {
    	return "Hello User";
    }

}
