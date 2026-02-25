package org.example.dial.controllers.contacts;

import org.example.dial.services.ContactsService;
import org.example.dial.utils.JwtGenerator;
import org.example.dial.utils.Option;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.example.dial.dto.ContactUpdate;

@Controller
@RequestMapping("contacts/update")
public class ContactUpdateController {
    private final JwtGenerator jwtGenerator;
    private final ContactsService contactsService;

    public ContactUpdateController(JwtGenerator jwtGenerator, ContactsService contactsService) {
        this.jwtGenerator = jwtGenerator;
        this.contactsService = contactsService;
    }

    @GetMapping("{oldContactName}")
    public String getUpdateContact(
        @CookieValue(name = "satisfy", defaultValue = "") String jwtToken,
        @PathVariable String oldContactName,
        Model model
    ) {
        if (jwtToken.equals(""))
            return "redirect:/auth/login";
        String name = this.jwtGenerator.parseName(jwtToken);
        if (name == null) 
            return "redirect:/auth/login";
        ContactUpdate dto = new ContactUpdate();
        dto.setNewContactName(oldContactName);
        model.addAttribute("dto", dto);
        model.addAttribute("oldContactName", oldContactName);
        return "contacts-update";
    }

    @PostMapping()
    public String updateContact(
        @CookieValue(name = "satisfy", defaultValue = "") String jwtToken,
        @ModelAttribute ContactUpdate dto,
        @RequestParam String oldContactName,
        Model model
    ) {
        if (jwtToken.equals(""))
            return "redirect:/auth/login";
        String name = this.jwtGenerator.parseName(jwtToken);
        if (name == null) 
            return "redirect:/auth/login";
        Option res = this.contactsService.updateContact(name, oldContactName, dto);
        if (!res.isSuccess()) {
            model.addAttribute("errorMessage", res.getError().getMessage());
            return "error";
        }
        return "redirect:/contacts"; 
    }
}
