package com.projekat.profesori_predmeti.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {

    private Long id;

    @NotBlank(message = "Ime je obavezno")
    @Size(min = 2, max = 50, message = "Ime mora imati 2-50 karaktera")
    private String firstName;

    @NotBlank(message = "Prezime je obavezno")
    @Size(min = 2, max = 50, message = "Prezime mora imati 2-50 karaktera")
    private String lastName;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Nevalidan format email adrese")
    private String email;

    @NotBlank(message = "Departman je obavezan")
    private String department;

    private String createdAt;
}
