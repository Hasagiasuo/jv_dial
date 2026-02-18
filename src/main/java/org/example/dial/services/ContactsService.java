package org.example.dial.services;

import java.util.stream.StreamSupport;

import org.example.dial.dto.ContactAdd;
import org.example.dial.errors.CannotSaveEntityError;
import org.example.dial.errors.ContactAlreadyExistsError;
import org.example.dial.errors.ContactNotFoundError;
import org.example.dial.errors.UserNotFoundError;
import org.example.dial.models.Contact;
import org.example.dial.models.User;
import org.example.dial.repositories.ContactRepository;
import org.example.dial.utils.Option;
import org.example.dial.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class ContactsService {
    private final ContactRepository contactRepository;
    private final UserCrudService userCrudService;

    public ContactsService(ContactRepository contactRepository, UserCrudService userCrudService) {
        this.contactRepository = contactRepository;
        this.userCrudService = userCrudService;
    }

    public boolean existsInUser(String ownerName, String contactName) {
        Result<Contact> contact = this.getContact(ownerName, contactName);
        return contact.isSuccess();
    }

    public Result<Contact> getContact(String ownerName, String contactName) {
        Result<User> owner = this.userCrudService.getByName(ownerName);
        if (!owner.isSuccess()) return new Result<Contact>(new UserNotFoundError(ownerName));
        for (Contact contact : this.getMyAll(owner.getValue().getName())) {
            if (contactName.equals(contact.getName())) return new Result<Contact>(contact);
        }
        return new Result<Contact>(new ContactNotFoundError(ownerName, contactName));
    }

    public Iterable<Contact> getMyAll(String name) {
        return StreamSupport.stream(this.contactRepository.findAll().spliterator(), false)
            .filter(u -> u.getOwner().getName().equals(name))
            .toList();
    }

    public Option removeContact(String ownerName, String contactName) {
        if (!this.existsInUser(ownerName, contactName)) return new Option(new ContactNotFoundError(ownerName, contactName));
        Result<Contact> contact = this.getContact(ownerName, contactName);
        if (!contact.isSuccess()) return new Option(contact.getError());
        this.contactRepository.delete(contact.getValue()); 
        return new Option();
    }

    public Option addContact(String ownerName, ContactAdd dto) {
        Result<User> owner = this.userCrudService.getByName(ownerName);
        if (this.existsInUser(ownerName, dto.getName())) 
            return new Option(new ContactAlreadyExistsError(ownerName, dto.getName()));
        Result<User> contact = this.userCrudService.getByName(dto.getContactName());
        if (!contact.isSuccess()) { return new Option(owner.getError()); }
        Contact newContact = new Contact();
        newContact.setOwner(owner.getValue());
        newContact.setContactUser(contact.getValue());
        newContact.setName(dto.getName());
        newContact.setIsFavorite(false);
        Contact addedContact = this.contactRepository.save(newContact);
        return addedContact != null ? new Option() : new Option(new CannotSaveEntityError("contacts"));
    }
}
