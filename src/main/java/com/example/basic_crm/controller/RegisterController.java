package com.example.basic_crm.controller;

import com.example.basic_crm.security.LoginCredentials;
import com.example.basic_crm.service.auth.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginCredentials loginCredentials) {
        registerService.registerNewUser(loginCredentials);
        return ResponseEntity.ok().build();
    }
}
