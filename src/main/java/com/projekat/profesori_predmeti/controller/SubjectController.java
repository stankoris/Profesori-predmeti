package com.projekat.profesori_predmeti.controller;

import com.projekat.profesori_predmeti.dto.SubjectDTO;
import com.projekat.profesori_predmeti.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAll() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    // GET /api/subjects/professor/{professorId}
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<SubjectDTO>> getByProfessor(
            @PathVariable Long professorId) {
        return ResponseEntity.ok(
                subjectService.getSubjectsByProfessor(professorId));
    }

    @PostMapping
    public ResponseEntity<SubjectDTO> create(
            @Valid @RequestBody SubjectDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subjectService.createSubject(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectDTO dto) {
        return ResponseEntity.ok(subjectService.updateSubject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}