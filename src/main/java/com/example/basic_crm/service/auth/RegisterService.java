package com.example.basic_crm.service.auth;

import com.example.basic_crm.exception.RegisterErrorException;
import com.example.basic_crm.model.User;
import com.example.basic_crm.repository.UserRepository;
import com.example.basic_crm.security.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(LoginCredentials loginCredentials) {
        if(userRepository.existsByUsername(loginCredentials.getUsername())) {
            throw new RegisterErrorException("User with username: " + loginCredentials.getUsername() + " already exists");
        }

        final User newUser = new User(loginCredentials.getUsername(), passwordEncoder.encode(loginCredentials.getPassword()), "user");
        return userRepository.save(newUser);
    }

}
