package org.example.dial.controllers;

import org.example.dial.utils.JwtGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class BaseController {
    private final JwtGenerator jwtGenerator;

    public BaseController(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping
    public String getHomePage(@CookieValue(name = "satisfy", defaultValue = "") String jwtToken, Model model) {
        String name = null;
        boolean isAuthenticated = false;
        String role = null;

        if (!jwtToken.isEmpty() && jwtGenerator.isValidToken(jwtToken)) {
            name = this.jwtGenerator.parseName(jwtToken);
            isAuthenticated = true;
            role = this.jwtGenerator.parseRole(jwtToken); // якщо Jwt містить роль
        }

        model.addAttribute("name", name);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("role", role);
        return "home";
    }
}