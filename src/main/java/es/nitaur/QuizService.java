package es.nitaur;

import java.util.Collection;
import java.util.List;

public interface QuizService {

    Collection<Quiz> findAll();

    Quiz findOne(Long id);

    Quiz create(Quiz quiz);

    Quiz update(Quiz quiz);

    void delete(Long id);

    QuizQuestion updateQuestion(Long id, QuizQuestion quiz);

    QuizQuestion answerQuestion(Long id, List<QuizAnswer> quizAnswers);

    QuizQuestion getQuestion(Long id);

    Collection<QuizQuestion> getAllQuestions();

    List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions);

    List<QuizQuestion> getQuestions(Long sectionId);
}
