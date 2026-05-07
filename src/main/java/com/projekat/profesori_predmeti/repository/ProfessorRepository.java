package com.projekat.profesori_predmeti.repository;

import com.projekat.profesori_predmeti.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByEmail(String email);
    boolean existsByEmail(String email);
    java.util.List<Professor> findByDepartment(String department);
}
