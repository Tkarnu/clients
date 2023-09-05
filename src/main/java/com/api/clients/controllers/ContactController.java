package com.api.clients.controllers;

import com.api.clients.enums.ContactType;
import com.api.clients.models.Contact;
import com.api.clients.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients/{clientId}/contacts")
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<?> addContact(@PathVariable Integer clientId, @RequestBody @Valid Contact contact, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        Contact createdContact = contactService.addContact(clientId, contact);
        return ResponseEntity.ok(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Integer clientId, @PathVariable Integer id, @RequestBody @Valid Contact updatedContact, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

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
    @GetMapping()
    public ResponseEntity<Optional<Contact>> getContactsByClientId(@PathVariable Integer clientId) {
        Optional<Contact> contacts = contactService.getContactsByClientId(clientId);
        if (contacts.isPresent()) {
            return ResponseEntity.ok(contacts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{contactType}")
    public ResponseEntity<Optional<Contact>> getContactsByClientIdAndContactType(@PathVariable Integer clientId, @PathVariable ContactType contactType) {
        Optional<Contact> contacts = contactService.getContactsByClientIdAndContactType(clientId, contactType);
        if (contacts.isPresent()) {
            return ResponseEntity.ok(contacts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}