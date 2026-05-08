package com.projekat.profesori_predmeti.service;

import com.projekat.profesori_predmeti.dto.SubjectDTO;
import com.projekat.profesori_predmeti.entity.*;
import com.projekat.profesori_predmeti.exception.ResourceNotFoundException;
import com.projekat.profesori_predmeti.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final ProfessorRepository professorRepository;

    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAllWithProfessor()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Predmet sa ID=" + id + " nije pronadjen"));
        return toDTO(subject);
    }

    public List<SubjectDTO> getSubjectsByProfessor(Long professorId) {
        return subjectRepository.findByProfessorId(professorId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubjectDTO createSubject(SubjectDTO dto) {
        Professor professor = professorRepository.findById(dto.getProfessorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Profesor sa ID=" + dto.getProfessorId() + " nije pronadjen"));
        Subject subject = Subject.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .espb(dto.getEspb())
                .semester(dto.getSemester())
                .professor(professor)
                .build();
        return toDTO(subjectRepository.save(subject));
    }

    @Transactional
    public SubjectDTO updateSubject(Long id, SubjectDTO dto) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Predmet sa ID=" + id + " nije pronadjen"));
        Professor professor = professorRepository.findById(dto.getProfessorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Profesor sa ID=" + dto.getProfessorId() + " nije pronadjen"));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setEspb(dto.getEspb());
        existing.setSemester(dto.getSemester());
        existing.setProfessor(professor);
        return toDTO(subjectRepository.save(existing));
    }

    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Predmet sa ID=" + id + " nije pronadjen");
        }
        subjectRepository.deleteById(id);
    }

    private SubjectDTO toDTO(Subject s) {
        return SubjectDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .description(s.getDescription())
                .espb(s.getEspb())
                .semester(s.getSemester())
                .professorId(s.getProfessor().getId())
                .professorName(s.getProfessor().getFirstName()
                        + " " + s.getProfessor().getLastName())
                .build();
    }
}
