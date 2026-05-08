package com.projekat.profesori_predmeti.controller;

import com.projekat.profesori_predmeti.dto.ProfessorDTO;
import com.projekat.profesori_predmeti.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/professors")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getAll() {
        return ResponseEntity.ok(professorService.getAllProfessors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.getProfessorById(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> create(
            @Valid @RequestBody ProfessorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professorService.createProfessor(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorDTO dto) {
        return ResponseEntity.ok(
                professorService.updateProfessor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }
}