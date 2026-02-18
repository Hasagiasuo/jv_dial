package org.example.dial.controllers.contacts;

import org.example.dial.services.ContactsService;
import org.example.dial.utils.JwtGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contacts")
public class ContactController {
    private final ContactsService contactsService;
    private final JwtGenerator jwtGenerator;

    public ContactController(ContactsService contactsService, JwtGenerator jwtGenerator) {
        this.contactsService = contactsService;
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping
    public String getMyContacts(@CookieValue(name = "satisfy", defaultValue = "") String jwtToken, Model model) {
        String name = this.jwtGenerator.parseName(jwtToken);
        model.addAttribute("contacts", this.contactsService.getMyAll(name));
        return "contacts-get";
    }
}