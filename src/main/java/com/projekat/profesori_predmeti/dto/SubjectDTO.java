package com.projekat.profesori_predmeti.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    private Long id;

    @NotBlank(message = "Naziv predmeta je obavezan")
    @Size(max = 100, message = "Naziv ne sme biti duzi od 100 karaktera")
    private String name;

    private String description;

    @NotNull(message = "ESPB je obavezan")
    @Min(value = 1, message = "ESPB mora biti najmanje 1")
    @Max(value = 60, message = "ESPB ne sme biti veci od 60")
    private Integer espb;

    @NotNull(message = "Semestar je obavezan")
    @Min(value = 1, message = "Semestar mora biti od 1 do 10")
    @Max(value = 10, message = "Semestar mora biti od 1 do 10")
    private Integer semester;

    @NotNull(message = "ID profesora je obavezan")
    private Long professorId;

    private String professorName;
}
