package es.nitaur.repository;

import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findBySection(QuizSection section);
}
