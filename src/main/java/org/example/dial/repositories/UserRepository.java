package org.example.dial.repositories;

import java.util.Optional;

import org.example.dial.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByName(String name);
    Optional<User> findByName(String name);
}