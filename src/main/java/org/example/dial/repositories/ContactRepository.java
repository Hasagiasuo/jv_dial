package org.example.dial.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import org.example.dial.models.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
}