package org.example.dial.dto;

import org.example.dial.models.UserRole;

public class UserAdd {
    private String name;
    private UserRole role;
    private String passwordHash;
    
    public String getName() { return this.name; }
    public void setName(String newName) { this.name = newName; }
    
    public String getPassowrdHash() { return this.passwordHash; }
    public void setPasswordHash(String newPasswordHash) { this.passwordHash = newPasswordHash; }
    
    public UserRole getRole() { return this.role; }
    public void setRole(UserRole newRole) { this.role = newRole; }
}