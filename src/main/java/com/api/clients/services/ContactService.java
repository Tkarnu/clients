package com.api.clients.services;

import com.api.clients.enums.ContactType;
import com.api.clients.models.Client;
import com.api.clients.models.Contact;
import com.api.clients.repositories.ClientRepository;
import com.api.clients.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ContactService {
    private final ContactRepository contactRepository;

    private final ClientRepository clientRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository, ClientRepository clientRepository) {
        this.contactRepository = contactRepository;
        this.clientRepository = clientRepository;
    }

    public Optional<Contact> getContactsByClientId(Integer clientId) {
        return contactRepository.findByClientId(clientId);
    }


    public Optional<Contact> getContactsByClientIdAndContactType(Integer clientId, ContactType contactType) {
        return contactRepository.findByClientIdAndContactType(clientId, contactType);
    }

    public Contact addContact(Integer clientId, Contact contact) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null) {
            ContactType contactType = determineContactType(contact.getContactValue());
            if (contactType != null) {
                contact.setContactType(contactType);
                contact.setClient(client);
                return contactRepository.save(contact);
            } else {
                throw new IllegalArgumentException("Invalid contactValue");
            }
        } else {
            throw new IllegalArgumentException("Client not found");
        }
    }

    public Contact updateContact(Integer clientId, Integer id, Contact updatedContact) {
        Client client = clientRepository.findById(clientId).orElse(null);
        Contact existingContact = contactRepository.findById(id).orElse(null);
        if (existingContact != null) {
            existingContact.setContactType(updatedContact.getContactType());
            existingContact.setContactValue(updatedContact.getContactValue());
            existingContact.setClient(client);
            return contactRepository.save(existingContact);
        }
        return null;
    }

    public boolean deleteContact(Integer clientId, Integer id) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null) {
            Optional<Contact> contactToDelete = contactRepository.findById(id);
        if (contactToDelete.isPresent() && contactToDelete.get().getClient().getId().equals(clientId)) {
            contactRepository.deleteById(id);
            return true;
        }
    }
            return false;
    }

    private ContactType determineContactType(String contactValue) {
        if (isValidPhoneNumber(contactValue)) {
            return ContactType.PHONE;
        } else if (isValidEmail(contactValue)) {
            return ContactType.EMAIL;
        } else {
            return ContactType.UNKNOWN;
        }
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?\\d{7,13}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z\\d+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

