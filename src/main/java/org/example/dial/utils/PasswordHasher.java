package org.example.dial.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
    private final BCryptPasswordEncoder encoder;

    public PasswordHasher() {
        this.encoder = new BCryptPasswordEncoder();
    }

    public String hashPassowrd(String password) {
        return this.encoder.encode(password);
    }
    
    public boolean isValidCompare(String password, String hash) {
        return this.encoder.matches(password, hash);
    }
}