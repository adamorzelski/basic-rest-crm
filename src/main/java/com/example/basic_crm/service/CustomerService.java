package com.example.basic_crm.service;

import com.example.basic_crm.model.Customer;
import com.example.basic_crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    public void saveAll(List<Customer> customers) {
        customerRepository.saveAll(customers);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public boolean existsById(int id) {
        return customerRepository.existsById(id);
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
}
