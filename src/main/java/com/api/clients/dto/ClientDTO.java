package com.api.clients.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientDTO {
    @NotBlank(message = "Имя клиента не может быть пустым")
    @Size(max = 255, message = "Имя клиента должно содержать не более 255 символов")
    private String name;
}
