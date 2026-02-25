package org.example.dial.controllers;

import org.example.dial.dto.UserAuth;
import org.example.dial.models.User;
import org.example.dial.services.AuthService;
import org.example.dial.services.UserCrudService;
import org.example.dial.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;

@Controller
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;
    private final UserCrudService userCrudService;

    public AuthController(AuthService authService, UserCrudService userCrudService) {
        this.authService = authService;
        this.userCrudService = userCrudService;
    }

    @GetMapping("login")
    public ModelAndView getLogin() {
        ModelAndView mv = new ModelAndView("auth-login");
        mv.addObject("dto", new UserAuth());
        return mv;
    }

    @PostMapping("login")
    public String postLogin(@Valid @ModelAttribute("dto") UserAuth dto, BindingResult bindingResult, Model model, HttpServletResponse resp) {
        if (bindingResult.hasErrors()) {
            return "auth-login";
        }

        Result<User> user = this.userCrudService.getByName(dto.getName());
        if (!user.isSuccess()) {
            model.addAttribute("errorMessage", user.getError().getMessage());
            return "auth-login";
        }

        Result<String> jwtRes = this.authService.login(dto, user.getValue().getPasswordHash(), user.getValue().getRole());
        if (!jwtRes.isSuccess()) {
            model.addAttribute("errorMessage", jwtRes.getError().getMessage());
            return "auth-login";
        }

        addJwtCookie(resp, jwtRes.getValue());
        return "redirect:/";
    }

    @GetMapping("register")
    public ModelAndView getRegister() {
        ModelAndView mv = new ModelAndView("auth-register");
        mv.addObject("dto", new UserAuth());
        return mv;
    }

    @PostMapping("register")
    public String postRegister(@Valid @ModelAttribute("dto") UserAuth dto, BindingResult bindingResult, Model model, HttpServletResponse resp) {
        if (bindingResult.hasErrors()) {
            return "auth-register";
        }
        if (this.userCrudService.existsByName(dto.getName())) {
            model.addAttribute("errorMessage", "User already exists! Please login");
            return "auth-register";
        }
        Result<String> jwtRes = this.authService.register(dto);
        if (!jwtRes.isSuccess()) {
            model.addAttribute("errorMessage", jwtRes.getError().getMessage());
            return "auth-register";
        }

        addJwtCookie(resp, jwtRes.getValue());
        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpServletResponse resp) {
        addJwtCookie(resp, "");
        return "redirect:/";
    }

    private void addJwtCookie(HttpServletResponse resp, String token) {
        Cookie jwtCookie = new Cookie("satisfy", token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(token.isEmpty() ? 0 : 60 * 60 * 24);
        resp.addCookie(jwtCookie);
    }
}