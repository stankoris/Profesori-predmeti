package com.projekat.profesori_predmeti.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank @Size(min=3, max=50)
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min=6)
    private String password;

//    private String role;
}