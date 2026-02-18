package org.example.dial.dto;

public class UserUpdate {
    private Long id;
    public void setId(Long newId) { this.id = newId; }
    public Long getId() { return this.id; }

    private String newName;
    public void setName(String newName) { this.newName = newName; }
    public String getName() { return this.newName; }
}