package com.projekat.profesori_predmeti.service;

import com.projekat.profesori_predmeti.dto.ProfessorDTO;
import com.projekat.profesori_predmeti.entity.Professor;
import com.projekat.profesori_predmeti.exception.ResourceNotFoundException;
import com.projekat.profesori_predmeti.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    public List<ProfessorDTO> getAllProfessors() {
        return professorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProfessorDTO getProfessorById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Profesor sa ID=" + id + " nije pronadjen"));
        return toDTO(professor);
    }

    @Transactional
    public ProfessorDTO createProfessor(ProfessorDTO dto) {
        if (professorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Profesor sa emailom " + dto.getEmail() + " vec postoji");
        }
        Professor professor = toEntity(dto);
        return toDTO(professorRepository.save(professor));
    }

    @Transactional
    public ProfessorDTO updateProfessor(Long id, ProfessorDTO dto) {
        Professor existing = professorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Profesor sa ID=" + id + " nije pronadjen"));
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setDepartment(dto.getDepartment());
        return toDTO(professorRepository.save(existing));
    }

    @Transactional
    public void deleteProfessor(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Profesor sa ID=" + id + " nije pronadjen");
        }
        professorRepository.deleteById(id);
    }

    private ProfessorDTO toDTO(Professor p) {
        return ProfessorDTO.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .department(p.getDepartment())
                .createdAt(p.getCreatedAt() != null
                        ? p.getCreatedAt().toString() : null)
                .build();
    }

    private Professor toEntity(ProfessorDTO dto) {
        return Professor.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .build();
    }
}
