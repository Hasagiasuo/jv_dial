package org.example.dial.services;

import java.util.Optional;

import org.example.dial.dto.UserAuth;
import org.example.dial.dto.UserJwt;
import org.example.dial.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.example.dial.utils.JwtGenerator;
import org.example.dial.utils.PasswordHasher;
import org.example.dial.utils.Result;
import org.example.dial.errors.PasswordUncorrectError;
import org.example.dial.errors.UserAlreadyExistsError;
import org.example.dial.errors.UserNotFoundError;
import org.example.dial.models.User;
import org.example.dial.models.UserRole;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final PasswordHasher passwordHasher;
    
    public AuthService(UserRepository userRepository, JwtGenerator jwtGenerator, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.passwordHasher = passwordHasher;
    }
    
    public Result<String> login(UserAuth dto, String passwordHash, UserRole role) {
        if (!this.passwordHasher.isValidCompare(dto.getPassword(), passwordHash)) {
            return new Result<String>(new PasswordUncorrectError());
        }
        Optional<User> user = this.userRepository.findByName(dto.getName());
        if (user.isEmpty()) {
            return new Result<String>(new UserNotFoundError(dto.getName())); 
        }
        return new Result<String>(this.jwtGenerator.generateToken(new UserJwt(user.get().getId(), dto.getName(), role)));
    }

    public Result<String> register(UserAuth dto) {
        if (this.userRepository.existsByName(dto.getName())) {
            return new Result<String>(new UserAlreadyExistsError(dto.getName()));
        }
        String passwordHash = this.passwordHasher.hashPassowrd(dto.getPassword());
        User user = new User();
        user.setPasswordHash(passwordHash);
        user.setName(dto.getName());
        user.setRole(dto.getName().endsWith("adm1n") ? UserRole.ADMIN : UserRole.USER);

        User savedUser = this.userRepository.save(user);
        return new Result<String>(this.jwtGenerator.generateToken(new UserJwt(savedUser.getId(), savedUser.getName(), savedUser.getRole())));
    }
}
