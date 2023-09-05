package com.api.clients.controllers;

import com.api.clients.enums.ContactType;
import com.api.clients.models.Contact;
import com.api.clients.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients/{clientId}/contacts")
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Contact> addContact(@PathVariable Integer clientId, @RequestBody @Valid Contact contact) {
        Contact createdContact = contactService.addContact(clientId, contact);
        return ResponseEntity.ok(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Integer clientId, @PathVariable Integer id, @RequestBody @Valid Contact updatedContact) {
        Contact updated = contactService.updateContact(clientId, id, updatedContact);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Integer clientId, @PathVariable Integer id) {
        boolean deleted = contactService.deleteContact(clientId, id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{clientId}")
    public ResponseEntity<List<Contact>> getContactsByClientId(@PathVariable Integer clientId) {
        List<Contact> contacts = contactService.getContactsByClientId(clientId);
        if (!contacts.isEmpty()) {
            return ResponseEntity.ok(contacts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{clientId}/{contactType}")
    public ResponseEntity<List<Contact>> getContactsByClientIdAndContactType(@PathVariable Integer clientId, @PathVariable ContactType contactType) {
        List<Contact> contacts = contactService.getContactsByClientIdAndContactType(clientId, contactType);
        if (!contacts.isEmpty()) {
            return ResponseEntity.ok(contacts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}