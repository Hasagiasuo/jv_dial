package org.example.dial.controllers.contacts;

import org.example.dial.services.ContactsService;
import org.example.dial.utils.JwtGenerator;
import org.example.dial.utils.Option;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("contacts/favorite")
public class IsFavoriteContactController {
    private final JwtGenerator jwtGenerator;
    private final ContactsService contactsService;

    public IsFavoriteContactController(JwtGenerator jwtGenerator, ContactsService contactsService) {
        this.jwtGenerator = jwtGenerator;
        this.contactsService = contactsService;
    }

    @PostMapping("{contactName}/{isFavorite}")
    public String changeIsFavoriteContact(
        @CookieValue(name = "satisfy", defaultValue = "") String jwtToken,
        @PathVariable String contactName,
        @PathVariable int isFavorite,
        Model model
    ) {
        if (jwtToken.equals("")) {
            return "redirect:/auth/login";
        }
        String name = this.jwtGenerator.parseName(jwtToken);
        if (name.equals("")) {
            return "redirect:/auth/login";
        }
        Option res = this.contactsService.updateIsFavoriteContact(name, contactName, isFavorite != 0);
        if (!res.isSuccess()) {
            model.addAttribute("errorMessage", res.getError().getMessage());
            return "contacts-get";
        }
        return "redirect:/contacts";
    }
}
