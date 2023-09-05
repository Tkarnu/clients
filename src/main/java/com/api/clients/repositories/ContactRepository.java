package com.api.clients.repositories;

import com.api.clients.enums.ContactType;
import com.api.clients.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Optional<Contact> findByClientId(Integer clientId);
    Optional<Contact> findByClientIdAndContactType(Integer clientId, ContactType contactType);
}
