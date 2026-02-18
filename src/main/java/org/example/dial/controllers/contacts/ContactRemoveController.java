package org.example.dial.controllers.contacts;

import org.example.dial.services.ContactsService;
import org.example.dial.utils.JwtGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.dial.utils.Option;

@Controller
@RequestMapping("/contacts/remove")
public class ContactRemoveController {

    private final ContactsService contactsService;
    private final JwtGenerator jwtGenerator;

    public ContactRemoveController(ContactsService contactsService, JwtGenerator jwtGenerator) {
        this.contactsService = contactsService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/{contactName}")
    public String removeContact(
            @CookieValue(name = "satisfy", defaultValue = "") String jwtToken,
            @PathVariable String contactName,
            Model model
    ) {
        String name = jwtGenerator.parseName(jwtToken);
        Option res = contactsService.removeContact(name, contactName);
        if (!res.isSuccess()) {
            model.addAttribute("errorMessage", res.getError().getMessage());
            return "error";
        }
        return "redirect:/contacts";
    }
}
