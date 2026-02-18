package org.example.dial.controllers.users;

import org.example.dial.utils.Result;
import org.example.dial.dto.UserUpdate;
import org.example.dial.models.User;
import org.example.dial.services.UserCrudService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("user/edit")
public class UserEditController {
    private final UserCrudService userCrudService;
    
    public UserEditController(UserCrudService userCrudService) {
        this.userCrudService = userCrudService;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ModelAndView getView(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user-edit");
        UserUpdate dto = new UserUpdate();
        dto.setId(id);
        mv.getModel().put("dto", dto);
        return mv;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String updateUser(@ModelAttribute UserUpdate dto, Model model) {
        Result<User> res = this.userCrudService.update(dto);
        if (!res.isSuccess()) {
            model.addAttribute("error-message", res.getError().getMessage());
            return "redirect:/user/edit";
        }
        return "redirect:/users";
    }
}