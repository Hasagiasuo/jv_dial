package org.example.dial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactAdd {
    @NotBlank
    @Size(min = 2, max = 16)
    private String name;

    @NotBlank
    @Size(min = 2, max = 16)
    private String contactName;

    public String getName() { return this.name; }
    public void setName(String newName) { this.name = newName; }

    public String getContactName() { return this.contactName; }
    public void setContactName(String newContactName) { this.contactName = newContactName; }
}
