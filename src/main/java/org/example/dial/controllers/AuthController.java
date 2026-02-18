package org.example.dial.controllers;

import org.example.dial.dto.UserAuth;
import org.example.dial.models.User;
import org.example.dial.services.AuthService;
import org.example.dial.services.UserCrudService;
import org.example.dial.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
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
        ModelAndView mv = new ModelAndView();
        mv.setViewName("auth-login");
        mv.getModel().put("dto", new UserAuth());
        return mv;
    }

    @PostMapping("login")
    public String postLogin(@ModelAttribute UserAuth dto, Model model, HttpServletResponse resp) {
        Result<User> user = this.userCrudService.getByName(dto.getName());
        if (!user.isSuccess()) {
            model.addAttribute("dto", dto);
            model.addAttribute("errorMessage", user.getError().getMessage());
            return "auth-login";
        }
        Result<String> jwtRes = this.authService.login(dto, user.getValue().getPasswordHash(), user.getValue().getRole());
        if (!jwtRes.isSuccess()) {
            model.addAttribute("dto", dto);
            model.addAttribute("errorMessage", jwtRes.getError().getMessage());
            return "auth-login";
        }
        Cookie jwtCookie = new Cookie("satisfy", jwtRes.getValue());
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(jwtCookie);
        return "redirect:/";
    }

    @GetMapping("register")
    public ModelAndView getRegister() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("auth-register.html");
        mv.getModel().put("dto", new UserAuth());
        return mv;
    }

    @PostMapping("register")
    public String postRegister(@ModelAttribute UserAuth dto, Model model, HttpServletResponse resp) {
        if (this.userCrudService.existsByName(dto.getName())) {
            model.addAttribute("dto", dto);
            model.addAttribute("errorMessage", "User already exist! Please login");
            return "auth-register";
        }
        Result<String> jwtRes = this.authService.register(dto);
        if (!jwtRes.isSuccess()) {
            model.addAttribute("dto", dto);
            model.addAttribute("errorMessage", jwtRes.getError().getMessage());
            return "auth-register";
        }
        Cookie jwtCookie = new Cookie("satisfy", jwtRes.getValue());
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(jwtCookie);
        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpServletResponse resp) {
        Cookie cookie = new Cookie("satisfy", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(cookie);
        return "redirect:/";
    }
}