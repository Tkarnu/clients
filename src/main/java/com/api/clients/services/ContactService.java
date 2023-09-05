package com.api.clients.services;

import com.api.clients.enums.ContactType;
import com.api.clients.models.Contact;
import com.api.clients.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public List<Contact> getContactsByClientId(Integer clientId) {

        return contactRepository.findByClientId(clientId);
    }

    public List<Contact> getContactsByClientIdAndContactType(Integer clientId, ContactType contactType) {
        return contactRepository.findByClientIdAndContactType(clientId, contactType);
    }

    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact updateContact(Integer id, Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id).orElse(null);
        if (existingContact != null) {
            existingContact.setContactType(updatedContact.getContactType());
            existingContact.setContactValue(updatedContact.getContactValue());
            return contactRepository.save(existingContact);
        }
        return null;
    }

    public boolean deleteContact(Integer id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

