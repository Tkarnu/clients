package com.api.clients.controllers;

import com.api.clients.dto.ContactDTO;
import com.api.clients.enums.ContactType;
import com.api.clients.models.Contact;
import com.api.clients.services.ContactService;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public ContactController(ContactService contactService, ModelMapper modelMapper) {
        this.contactService = contactService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<?> addContact(@PathVariable Integer clientId, @RequestBody @Valid ContactDTO contactDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        Contact contact = modelMapper.map(contactDTO, Contact.class);
        Contact createdContact = contactService.addContact(clientId, contact);
        ContactDTO createdContactDTO = modelMapper.map(createdContact, ContactDTO.class);
        return ResponseEntity.ok(createdContactDTO);
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
    public ResponseEntity<List<ContactDTO>> getContactsByClientId(@PathVariable Integer clientId) {
        Optional<List<Contact>> optionalContacts = contactService.getContactsByClientId(clientId);
        if (optionalContacts.isPresent()) {
            List<Contact> contacts = optionalContacts.get();
            List<ContactDTO> contactDTOs = contacts.stream()
                    .map(contact -> modelMapper.map(contact, ContactDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(contactDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{contactType}")
    public ResponseEntity<List<ContactDTO>> getContactsByClientIdAndContactType(@PathVariable Integer clientId, @PathVariable String contactType) {
        contactType = contactType.trim().toUpperCase();

        try {
            ContactType type = ContactType.valueOf(contactType);
            Optional<List<Contact>> contacts = contactService.getContactsByClientIdAndContactType(clientId, type);

            if (contacts.isPresent()) {
                List<Contact> contactList = contacts.get();
                List<ContactDTO> contactDTOs = contactList.stream()
                        .map(contact -> modelMapper.map(contact, ContactDTO.class))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(contactDTOs);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}