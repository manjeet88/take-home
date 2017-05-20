package es.nitaur.repository;

import es.nitaur.domain.QuizSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSectionRepository extends JpaRepository<QuizSection, Long> {

}
