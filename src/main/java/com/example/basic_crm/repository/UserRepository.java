package com.example.basic_crm.repository;

import com.example.basic_crm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    User save(User user);

    boolean existsByUsername(String username);
}
