package com.example.basic_crm.controller;

import com.example.basic_crm.model.Customer;
import com.example.basic_crm.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    public static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        logger.info("Get all customers.");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int customerId) {
        logger.info("Get customer by id = " + customerId + ".");
        return customerService.getCustomerById(customerId)
                .map(customer -> ResponseEntity.ok(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        logger.info("Save customer.");

        if (savedCustomer == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(savedCustomer.getId()).toUri();

        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable int id, @RequestBody Customer toUpdate) {
        logger.info("Update customer. Id = " + id);
        if(!customerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerService.getCustomerById(id)
                .ifPresent(customer -> {
                    customer.updateFrom(toUpdate);
                    customerService.saveCustomer(customer);
                });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int id) {
        logger.info("Delete customer. Id = " + id);
        if(!customerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

}
















