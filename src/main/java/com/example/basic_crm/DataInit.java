package com.example.basic_crm;

import com.example.basic_crm.model.Customer;
import com.example.basic_crm.model.User;
import com.example.basic_crm.repository.UserRepository;
import com.example.basic_crm.service.domain.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInit {

    @Autowired
    public DataInit(CustomerService customerService, UserRepository userRepository, PasswordEncoder passwordEncoder) {

        List<Customer> customers = Arrays.asList(
                new Customer("John", "Doe", "john.doe@mail.com", "Main 1", "Lublin", "123456789"),
                new Customer("Adam", "Smith", "adam.smith@mail.com", "Main 2", "Warszawa", "223456789"),
                new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789")
        );

        customerService.saveAll(customers);

        User user = new User("user", passwordEncoder.encode("user"), "user");

        userRepository.save(user);
    }
}
