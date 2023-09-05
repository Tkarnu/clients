package com.api.clients.services;

import com.api.clients.enums.ContactType;
import com.api.clients.models.Client;
import com.api.clients.models.Contact;
import com.api.clients.repositories.ClientRepository;
import com.api.clients.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
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

    public List<Contact> getContactsByClientId(Integer clientId) {

        return contactRepository.findByClientId(clientId);
    }


    public List<Contact> getContactsByClientIdAndContactType(Integer clientId, ContactType contactType) {
        return contactRepository.findByClientIdAndContactType(clientId, contactType);
    }


    public Contact addContact(Integer clientId, Contact contact) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null) {
            ContactType contactType = determineContactType(contact.getContactValue());
            if (contactType != null) {
                contact.setContactType(contactType);
                contact.setClient(client); // Устанавливаем связь с клиентом
                return contactRepository.save(contact);
            } else {
                throw new IllegalArgumentException("Invalid contactValue");
            }
        } else {
            throw new IllegalArgumentException("Client not found");
        }
    }

    public Contact updateContact(Integer clientId, Integer id, Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id).orElse(null);
        if (existingContact != null) {
            existingContact.setContactType(updatedContact.getContactType());
            existingContact.setContactValue(updatedContact.getContactValue());
            return contactRepository.save(existingContact);
        }
        return null;
    }

    public boolean deleteContact(Integer clientId, Integer id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
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
        String regex = "^[0-9+\\-\\s]{7,11}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

