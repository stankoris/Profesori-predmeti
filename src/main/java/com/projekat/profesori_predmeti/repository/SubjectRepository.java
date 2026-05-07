package com.projekat.profesori_predmeti.repository;

import com.projekat.profesori_predmeti.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByProfessorId(Long professorId);
    List<Subject> findBySemester(Integer semester);
    boolean existsByName(String name);

    @Query("SELECT s FROM Subject s JOIN FETCH s.professor")
    List<Subject> findAllWithProfessor();
}
