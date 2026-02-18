package org.example.dial.services;

import java.util.Optional;
import java.util.stream.StreamSupport;

import org.example.dial.dto.UserAdd;
import org.example.dial.dto.UserUpdate;
import org.example.dial.errors.UserAlreadyExistsError;
import org.example.dial.errors.UserNotFoundError;
import org.example.dial.models.User;
import org.example.dial.models.UserRole;
import org.example.dial.repositories.UserRepository;
import org.example.dial.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class UserCrudService{
    private final UserRepository userRepository;

    public UserCrudService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsByName(String name) { return this.userRepository.existsByName(name); }

    public Result<User> add(UserAdd dto) {
        if (this.userRepository.existsByName(dto.getName())) {
            return new Result<User>(new UserAlreadyExistsError(dto.getName()));
        }
        User user = new User();
        user.setName(dto.getName());
        user.setRole(dto.getRole());
        user.setPasswordHash(dto.getPassowrdHash());
        User addedUser = this.userRepository.save(user);
        return new Result<User>(addedUser);
    }

    public Result<User> update(UserUpdate dto) {
        Optional<User> user = this.userRepository.findById(dto.getId());
        if (user.isEmpty()) {
            return new Result<User>(new UserNotFoundError(dto.getName()));
        }
        user.get().setName(dto.getName());
        User updatedUser = this.userRepository.save(user.get());
        return new Result<User>(updatedUser);
    }

    public Result<User> getByName(String name) {
        Optional<User> user = this.userRepository.findByName(name);
        if (user.isEmpty()) {
            return new Result<User>(new UserNotFoundError(name));
        } else {
            return new Result<User>(user.get());
        }
    }

    public Iterable<User> getAllWithinAdmins() {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
            .filter(u -> u.getRole() != UserRole.ADMIN)
            .toList();
    }

    public Iterable<User> getAllWithinMe(String selfName) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .filter(u -> !u.getName().equals(selfName))
            .toList();
    }

    public Result<User> getById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            return new Result<User>(new UserNotFoundError(id.toString()));
        } else {
            return new Result<User>(user.get());
        }
    }

    public Iterable<User> getAll() {
        return this.userRepository.findAll();
    }

    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }
}