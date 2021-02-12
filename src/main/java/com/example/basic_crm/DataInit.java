package com.example.basic_crm;

import com.example.basic_crm.model.Customer;
import com.example.basic_crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInit {

    @Autowired
    public DataInit(CustomerService customerService) {

        List<Customer> customers = Arrays.asList(
                new Customer("John", "Doe", "john.doe@mail.com", "Main 1", "Lublin", "123456789"),
                new Customer("Adam", "Smith", "adam.smith@mail.com", "Main 2", "Warszawa", "223456789"),
                new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789")
        );

        customerService.saveAll(customers);
    }
}
