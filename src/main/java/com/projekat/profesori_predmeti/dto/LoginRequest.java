package com.projekat.profesori_predmeti.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username je obavezan")
    private String username;

    @NotBlank(message = "Password je obavezan")
    private String password;
}