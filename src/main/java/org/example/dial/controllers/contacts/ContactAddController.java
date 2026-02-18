package org.example.dial.controllers.contacts;

import org.example.dial.dto.ContactAdd;
import org.example.dial.services.ContactsService;
import org.example.dial.utils.JwtGenerator;
import org.example.dial.utils.Option;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contacts/add")
public class ContactAddController {
    private final ContactsService contactsService;
    private final JwtGenerator jwtGenerator;
    
    public ContactAddController(ContactsService contactsService, JwtGenerator jwtGenerator) {
        this.contactsService = contactsService;
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping 
    public String getAddContact(Model model) {
        model.addAttribute("dto", new ContactAdd()); 
        return "contact-add";
    }

    @PostMapping
    public String postAddContact(
            @CookieValue(name = "satisfy", defaultValue = "") String jwtToken,
            @ModelAttribute ContactAdd dto,
            Model model
    ) {
        String name = this.jwtGenerator.parseName(jwtToken);

        Option addContactOpt = this.contactsService.addContact(name, dto);

        if (!addContactOpt.isSuccess()) {
            model.addAttribute("errorMessage", addContactOpt.getError().getMessage());
            model.addAttribute("dto", dto);  
            return "contact-add";
        }

        return "redirect:/contacts"; 
    }
}
