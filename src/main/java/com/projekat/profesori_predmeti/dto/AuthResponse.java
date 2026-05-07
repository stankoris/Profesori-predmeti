package com.projekat.profesori_predmeti.dto;

import lombok.*;

@Data

@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String username;
    private String role;
    private String message;
}