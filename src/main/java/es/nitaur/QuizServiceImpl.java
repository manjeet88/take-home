package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import es.nitaur.repository.QuizQuestionRepository;
import es.nitaur.repository.QuizRepository;
import es.nitaur.repository.QuizSectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;
    @Autowired
    private QuizSectionRepository quizSectionRepository;

    @Override
    public Collection<Quiz> findAll() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz findOne(final Long id) {
        return quizRepository.findOne(id);
    }

    @Override
    public Quiz create(final Quiz quiz) {
        if (quiz.getId() != null) {
            logger.error("Attempted to create a Quiz, but id attribute was not null.");
            throw new EntityExistsException(
                    "Cannot create new Quiz with supplied id. The id attribute must be null to create an entity.");
        }

        for (QuizSection section : quiz.getSections()) {
            section.setQuiz(quiz);
            section.getQuizQuestions().forEach(it -> it.setSection(section));
        }

        return quizRepository.save(quiz);
    }

    @Override
    public Quiz update(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public void delete(final Long id) {
        quizRepository.delete(id);
    }

    @Override
    public QuizQuestion updateQuestion(Long id, QuizQuestion question) {
        QuizQuestion questionToUpdate = tryFindQuestionById(id);
        questionToUpdate.setQuestion(question.getQuestion());
        questionToUpdate.setAnswers(question.getAnswers());
        return quizQuestionRepository.save(questionToUpdate);
    }

    @Override
    public QuizQuestion answerQuestion(Long id, List<QuizAnswer> quizAnswers) {
        QuizQuestion questionToUpdate = quizQuestionRepository.findOne(id);
        if (questionToUpdate == null) {
            logger.error("Attempted to answer Question, but Question is not found");
            throw new NoResultException(
                    "Cannot answer Question with supplied id. The object is not found.");
        }
        questionToUpdate.setAnswers(quizAnswers);
        questionToUpdate.setUpdateCount(questionToUpdate.getUpdateCount() + 1);
        return quizQuestionRepository.save(questionToUpdate);
    }

    @Override
    public QuizQuestion getQuestion(Long id) {
        return quizQuestionRepository.findOne(id);
    }

    @Override
    public Collection<QuizQuestion> getAllQuestions() {
        return quizQuestionRepository.findAll();
    }

    @Override
    @Transactional
    public List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions) {
        List<QuizQuestion> updatedQuestions = Lists.newArrayList();
        for (QuizQuestion quizQuestion : quizQuestions) {
            QuizQuestion questionToUpdate = tryFindQuestionById(quizQuestion.getId());
            questionToUpdate.setQuestion(quizQuestion.getQuestion());
            updatedQuestions.add(quizQuestionRepository.save(questionToUpdate));
        }
        return updatedQuestions;
    }

    @Override
    public List<QuizQuestion> getQuestions(Long filterBySectionId) {
        QuizSection quizSection = quizSectionRepository.findOne(filterBySectionId);
        if (quizSection == null) {
            logger.error("Attempted to get Questions by Section id, but Quiz Section is not found");
            throw new NoResultException(
                    "Cannot find Questions with supplied Quiz Section id. The object is not found.");
        }
        return quizQuestionRepository.findBySection(quizSection);
    }

    private QuizQuestion tryFindQuestionById(Long id) {
        QuizQuestion questionToUpdate = quizQuestionRepository.findOne(id);
        if (questionToUpdate == null) {
            logger.error("Attempted to update Question, but Question is not found");
            throw new NoResultException(
                    "Cannot update Question with supplied id. The object is not found.");
        }
        return questionToUpdate;
    }
}
