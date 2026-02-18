package org.example.dial.models;

import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;     // у кого список контактів

    @ManyToOne
    @JoinColumn(name = "contact_user_id")
    private User contactUser;  // сам контакт

    private String name;
    private boolean isFavorite;

    public User getOwner() { return this.owner; }
    public void setOwner(User newOwner) { this.owner = newOwner; }

    public User getContactUser() { return contactUser; }
    public void setContactUser(User newContactUser) { this.contactUser = newContactUser; }

    public Long getId() { return this.id; }
    public void setId(Long newId) { this.id = newId; }

    public String getName() { return this.name; }
    public void setName(String newName) { this.name = newName; }

    public boolean getIsFavorite() { return this.isFavorite; }
    public void setIsFavorite(boolean newIsFavorite) { this.isFavorite = newIsFavorite; }
}
