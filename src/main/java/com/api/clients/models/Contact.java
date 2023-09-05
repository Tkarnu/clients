package com.api.clients.models;

import com.api.clients.enums.ContactType;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "contacts")
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;



    @NotBlank(message = "Значение контакта не может быть пустым")
    @Pattern(
            regexp ="^\\+?\\d{7,11}|[A-Za-z0-9._%+-]+@[A-Za-z0-9.-_+%]+\\.[A-Za-z]{2,4}$",
            message = "Значение контакта должно быть допустимым email-адресом или номером телефона"
    )
    @Column(name = "contact_value")
    private String contactValue;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
}

