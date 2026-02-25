package org.example.dial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserAuth {

    @NotBlank(message = "Ім'я не може бути порожнім")
    @Size(min = 2, max = 16, message = "Ім'я має бути від 2 до 16 символів")
    private String name;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 8, message = "Пароль має бути не менше 8 символів")
    private String password;
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }
}