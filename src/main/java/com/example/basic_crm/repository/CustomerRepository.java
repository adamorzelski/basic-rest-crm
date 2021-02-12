package com.example.basic_crm.repository;

import com.example.basic_crm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findAll();

    Optional<Customer> findById(Integer integer);

    <S extends Customer> List<S> saveAll(Iterable<S> iterable);

    Customer save(Customer customer);

    boolean existsById(Integer id);

    void deleteById(Integer integer);

}
