package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class QuizControllerTest extends AbstractQuizApiTest {

    @Test
    public void getQuizzes_returnNoQuizzesIfNoQuizzes() throws Exception {
        ResponseEntity<List<Quiz>> exchange = httpGetList(QUIZZES_API, QUESTIONS_TYPE);

        List<Quiz> quizzes = exchange.getBody();

        assertThat("Quizzes not found, return status 200", exchange.getStatusCode().value(), equalTo(HttpStatus.SC_OK));
        assertThat("Return 0 quizzes", quizzes, hasSize(0));
    }

    @Test
    public void getQuizzes_returnAllQuizzes() throws Exception {
        saveQuize1();
        saveQuize2();

        ResponseEntity<List<Quiz>> exchange = httpGetList(QUIZZES_API, QUIZZES_TYPE);
        List<Quiz> quizzes = exchange.getBody();

        assertThat("Quizzes found, return status 200", exchange.getStatusCode().value(), equalTo(HttpStatus.SC_OK));
        assertThat("Return 2 quizzes", quizzes, hasSize(2));
    }

    @Test
    public void getQuiz_returnQuizById() throws Exception {
        Quiz savedQuiz = saveQuize1();

        ResponseEntity<Quiz> response = httpGetOne(QUIZZES_API + "/" + savedQuiz.getId(), Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz found, return status 200", response.getStatusCode().value(), equalTo(HttpStatus.SC_OK));
        assertThat("Return quiz with name " + savedQuiz.getName(), quiz.getName(), equalTo(savedQuiz.getName()));
    }

    @Test
    public void getQuiz_quizNotFound() throws Exception {
        ResponseEntity<Quiz> response = httpGetOne(QUIZZES_API + "/" + 1, Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz not found, return status 404", response.getStatusCode().value(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Quiz not returned", quiz, nullValue());
    }

    @Test
    public void createQuiz() throws Exception {
        Quiz quizToCreate = buildQuize();

        ResponseEntity<Quiz> response = httpPostOne(QUIZZES_API, quizToCreate, Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz created, return status 201", response.getStatusCode().value(), equalTo(HttpStatus.SC_CREATED));
        assertThat("Created quiz has name " + quizToCreate.getName(), quiz.getName(), equalTo(quizToCreate.getName()));
    }

    @Test
    public void deleteQuiz() throws Exception {
        Quiz savedQuiz = saveQuize1();

        ResponseEntity<Quiz> response = httpDelete(QUIZZES_API + "/" + savedQuiz.getId(), Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz deleted, return status 204", response.getStatusCode().value(), equalTo(HttpStatus.SC_NO_CONTENT));
        assertThat("Quiz not returned", quiz, nullValue());
    }

    @Test
    public void updateQuestion() throws Exception {
        Quiz savedQuiz = saveQuize1();
        QuizQuestion savedQuestion = savedQuiz.getSections().iterator().next().getQuizQuestions().iterator().next();

        QuizQuestion questionToUpdate = new QuizQuestion();
        questionToUpdate.setQuestion("What's up?");

        httpPutOne(QUESTIONS_API + "/" + savedQuestion.getId(), questionToUpdate);

        ResponseEntity<QuizQuestion> response = httpGetOne(QUESTIONS_API + "/" + savedQuestion.getId(), QuizQuestion.class);
        QuizQuestion question = response.getBody();

        assertThat("Updated question text is \"What's up?\"", question.getQuestion(), equalTo("What's up?"));
    }

    @Test
    public void updateQuestions() throws Exception {
        Quiz savedQuiz = saveQuize1();
        QuizSection savedSection = savedQuiz.getSections().iterator().next();
        Iterator<QuizQuestion> iterator = savedSection.getQuizQuestions().iterator();
        QuizQuestion savedQuestion1 = iterator.next();
        QuizQuestion savedQuestion2 = iterator.next();

        QuizQuestion questionToUpdate1 = new QuizQuestion();
        questionToUpdate1.setId(savedQuestion1.getId());
        questionToUpdate1.setQuestion("What's up?");
        QuizQuestion questionToUpdate2 = new QuizQuestion();
        questionToUpdate2.setId(savedQuestion2.getId());
        questionToUpdate2.setQuestion("What's up?");
        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(questionToUpdate1, questionToUpdate2);

        httpPutList(QUESTIONS_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> response = httpGetList(QUESTIONS_API + "?filterSectionId=" + savedSection.getId(), QUESTIONS_TYPE);
        List<QuizQuestion> questions = response.getBody();

        for (QuizQuestion question : questions) {
            assertThat("Updated question text is \"What's up?\"", question.getQuestion(), equalTo("What's up?"));
        }
        assertThat("Return 2 questions", questions, hasSize(2));
    }

    //TODO: other endpoints

}