package com.api.clients.dto;

import com.api.clients.enums.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ContactDTO {
    @NotBlank(message = "Значение контакта не может быть пустым")
    @Pattern(
            regexp ="^(\\+?\\d{7,11}" +
                    "|[A-Za-z0-9._%+-]+@[A-Za-z0-9.-_+%]+\\.[A-Za-z]{2,4})$",
            message = "Значение контакта должно быть допустимым email-адресом или номером телефона"
    )
    private String contactValue;

    private ContactType contactType;
}
