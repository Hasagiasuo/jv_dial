package org.example.dial.controllers.users;

import org.example.dial.models.User;
import org.example.dial.services.UserCrudService;
import org.example.dial.utils.JwtGenerator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class UsersController {
    private final UserCrudService userCrudService;
    private final JwtGenerator jwtGenerator;

    public UsersController(UserCrudService userCrudService, JwtGenerator jwtGenerator) {
        this.userCrudService = userCrudService;
        this.jwtGenerator = jwtGenerator;
    }
   
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getUsersView(@CookieValue(name = "satisfy", defaultValue = "") String jwtToken, Model model) {
        if (jwtToken.equals("")) {
            return "redirect:/";
        }
        String name = this.jwtGenerator.parseName(jwtToken);
        if (name != null && name.equals("")) {
            model.addAttribute("errorMessage", "error authentification");
            return "users-get";
        }
        Iterable<User> allUsers = this.userCrudService.getAllWithinAdmins();
        model.addAttribute("users", allUsers);
        return "users-get";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Long id) {
        this.userCrudService.deleteById(id);
        return "redirect:/users";
    }
}