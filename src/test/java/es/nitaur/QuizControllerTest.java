package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class QuizControllerTest extends AbstractQuizApiTest {

    @Test
    public void getQuizzes_returnNoQuizzesIfNoQuizzes() throws Exception {
        cleanUp();
        ResponseEntity<List<Quiz>> exchange = httpGetList(QUIZZES_API, QUESTIONS_TYPE);

        List<Quiz> quizzes = exchange.getBody();

        assertThat("Quizzes not found, return status 200", exchange.getStatusCode().value(), equalTo(HttpStatus.SC_OK));
        assertThat("Return 0 quizzes", quizzes, hasSize(0));
    }

    @Test
    public void getQuizzes_returnAllQuizzes() throws Exception {
        cleanUp();
        saveQuiz();
        saveQuiz();

        ResponseEntity<List<Quiz>> exchange = httpGetList(QUIZZES_API, QUIZZES_TYPE);
        List<Quiz> quizzes = exchange.getBody();

        assertThat("Quizzes found, return status 200", exchange.getStatusCode().value(), equalTo(HttpStatus.SC_OK));
        assertThat("Return 2 quizzes", quizzes, hasSize(2));
    }

    @Test
    public void getQuiz_returnQuizById() throws Exception {
        Quiz savedQuiz = saveQuiz();

        ResponseEntity<Quiz> response = httpGetOne(QUIZZES_API + "/" + savedQuiz.getId(), Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz found, return status 200", response.getStatusCode().value(), equalTo(HttpStatus.SC_OK));
        assertThat("Return quiz with name " + savedQuiz.getName(), quiz.getName(), equalTo(savedQuiz.getName()));
    }

    @Test
    public void getQuiz_quizNotFound() throws Exception {
        ResponseEntity<Quiz> response = httpGetOne(QUIZZES_API + "/" + -1, Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz not found, return status 404", response.getStatusCode().value(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Quiz not returned", quiz, nullValue());
    }

    @Test
    public void createQuiz() throws Exception {
        Quiz quizToCreate = buildQuiz();

        ResponseEntity<Quiz> response = httpPostOne(QUIZZES_API, quizToCreate, Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz created, return status 201", response.getStatusCode().value(), equalTo(HttpStatus.SC_CREATED));
        assertThat("Created quiz has name " + quizToCreate.getName(), quiz.getName(), equalTo(quizToCreate.getName()));
    }

    @Test
    public void deleteQuiz() throws Exception {
        Quiz savedQuiz = saveQuiz();

        ResponseEntity<Quiz> response = httpDelete(QUIZZES_API + "/" + savedQuiz.getId(), Quiz.class);
        Quiz quiz = response.getBody();

        assertThat("Quiz deleted, return status 204", response.getStatusCode().value(), equalTo(HttpStatus.SC_NO_CONTENT));
        assertThat("Quiz not returned", quiz, nullValue());
    }

    @Test
    public void updateQuestion() throws Exception {
        QuizQuestion savedQuestion = saveQuizWithQuestion("OK?");

        QuizQuestion questionToUpdate = QuizQuestion.newBuilder()
                .question("What's up?")
                .build();

        httpPutOne(QUESTIONS_API + "/" + savedQuestion.getId(), questionToUpdate);

        ResponseEntity<QuizQuestion> response = httpGetOne(QUESTIONS_API + "/" + savedQuestion.getId(), QuizQuestion.class);
        QuizQuestion question = response.getBody();

        assertThat("Updated question text is \"What's up?\"", question.getQuestion(), equalTo("What's up?"));
    }

    @Test
    public void updateQuestions() throws Exception {
        QuizSection savedSection = saveQuizWithSection("Question 1?", "Question 2?");
        List<QuizQuestion> savedQuestions = savedSection.getQuizQuestions();
        QuizQuestion savedQuestion1 = savedQuestions.get(0);
        QuizQuestion savedQuestion2 = savedQuestions.get(1);
        Long sectionId = savedSection.getId();

        QuizQuestion questionToUpdate1 = QuizQuestion.newBuilder()
                .id(savedQuestion1.getId())
                .question("What's up?")
                .build();
        QuizQuestion questionToUpdate2 = QuizQuestion.newBuilder()
                .id(savedQuestion2.getId())
                .question("What's up?")
                .build();
        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(questionToUpdate1, questionToUpdate2);

        httpPutList(QUESTIONS_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> response = httpGetList(QUESTIONS_API + "?filterSectionId=" + sectionId, QUESTIONS_TYPE);
        List<QuizQuestion> questions = response.getBody();

        for (QuizQuestion question : questions) {
            assertThat("Updated question text is \"What's up?\"", question.getQuestion(), equalTo("What's up?"));
        }
        assertThat("Return 2 questions", questions, hasSize(2));
    }

    //TODO: other endpoints

}