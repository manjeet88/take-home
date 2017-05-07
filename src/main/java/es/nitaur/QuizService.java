package es.nitaur;

import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;

import java.util.Collection;
import java.util.List;

public interface QuizService {

    Collection<Quiz> findAll();

    Quiz findOne(Long id);

    Quiz create(Quiz quiz);

    //TODO: remove method or add respective endpoint
    Quiz update(Quiz quiz);

    void delete(Long id);

    QuizQuestion updateQuestion(Long id, QuizQuestion quiz);

    QuizQuestion answerQuestion(Long id, List<QuizAnswer> quizAnswers);

    QuizQuestion getQuestion(Long id);

    Collection<QuizQuestion> getAllQuestions();

    List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions);

    List<QuizQuestion> getQuestions(Long sectionId);
}
