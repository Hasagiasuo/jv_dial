package org.example.dial.dto;

import org.example.dial.models.UserRole;

public class UserJwt {
    private Long id;
    private String name;
    private UserRole role;
    
    public UserJwt() {}
    
    public UserJwt(Long id, String name, UserRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return this.id; }
    public String getName() { return this.name; }
    public UserRole getRole() { return this.role; }
    
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRole(UserRole role) { this.role = role; }
}