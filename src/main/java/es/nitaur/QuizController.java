package es.nitaur;

import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private QuizService quizService;

    @GetMapping(value = "/quizzes")
    public ResponseEntity<Collection<Quiz>> getQuizzes() {
        final Collection<Quiz> quizzes = quizService.findAll();
        return new ResponseEntity<Collection<Quiz>>(quizzes, HttpStatus.OK);
    }

    @GetMapping(value = "/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable final Long id) {
        final Quiz quiz = quizService.findOne(id);
        if (quiz == null) {
            logger.info("Requested Quiz not found.");
            return new ResponseEntity<Quiz>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Quiz>(quiz, HttpStatus.OK);
    }

    @PostMapping(value = "/quizzes",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> createQuiz(@RequestBody final Quiz quiz) {
        final Quiz savedQuiz = quizService.create(quiz);
        return new ResponseEntity<Quiz>(savedQuiz, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/quizzes/{id}")
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable("id") final Long id) {
        quizService.delete(id);
        return new ResponseEntity<Quiz>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/questions/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizQuestion> updateQuestion(@PathVariable("id") final Long id, @RequestBody final QuizQuestion quizQuestion) {
        QuizQuestion updatedQuestion = quizService.updateQuestion(id, quizQuestion);
        return new ResponseEntity<QuizQuestion>(updatedQuestion, HttpStatus.OK);
    }

    @PutMapping(value = "/questions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuizQuestion>> updateQuestions(@RequestBody final List<QuizQuestion> quizQuestions) {
        List<QuizQuestion> updatedQuestions = quizService.updateQuestions(quizQuestions);
        return new ResponseEntity<List<QuizQuestion>>(updatedQuestions, HttpStatus.OK);
    }

    @GetMapping(value = "/questions/{id}")
    public ResponseEntity<QuizQuestion> getQuestion(@PathVariable("id") final Long id) {
        final QuizQuestion question = quizService.getQuestion(id);
        if (question == null) {
            logger.info("Requested Question not found.");
            return new ResponseEntity<QuizQuestion>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<QuizQuestion>(question, HttpStatus.OK);
    }

    @GetMapping(value = "/questions")
    public ResponseEntity<Collection<QuizQuestion>> getQuestions(@RequestParam(value = "filterSectionId", required = false) Long filterBySectionId) {
        Collection<QuizQuestion> questions;
        if (filterBySectionId != null) {
            questions = quizService.getQuestions(filterBySectionId);
        } else {
            questions = quizService.getAllQuestions();
        }
        return new ResponseEntity<Collection<QuizQuestion>>(questions, HttpStatus.OK);
    }

    @PostMapping(value = "/questions/{id}/answers",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizQuestion> answerQuestion(@PathVariable("id") final Long id, @RequestBody final List<QuizAnswer> quizAnswers) {
        QuizQuestion updatedQuestion = quizService.answerQuestion(id, quizAnswers);
        return new ResponseEntity<QuizQuestion>(updatedQuestion, HttpStatus.OK);
    }
}
