package org.example.dial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactUpdate {
    @NotBlank
    @Size(min = 2)
    private String newContactName;

    public String getNewContactName() { return this.newContactName; }
    public void setNewContactName(String newContactName) { this.newContactName = newContactName; }
}
