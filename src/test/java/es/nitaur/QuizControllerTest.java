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
        Long questionId = saveQuizWithQuestion("OK?");

        String newText = "What's up?";
        QuizQuestion questionToUpdate = QuizQuestion.newBuilder()
                .question(newText)
                .build();

        httpPutOne(QUESTIONS_API + "/" + questionId, questionToUpdate);

        QuizQuestion question = getOne(QUESTIONS_API + "/" + questionId, QuizQuestion.class);

        assertThat("Updated question text is " + newText, question.getQuestion(), equalTo(newText));
    }

    @Test
    public void updateQuestions() throws Exception {
        QuizSection section = saveQuizWithSection("Question 1?", "Question 2?");

        String newText = "What's up?";
        QuizQuestion questionToUpdate1 = QuizQuestion.newBuilder()
                .id(section.getFirstQuestionId())
                .question(newText)
                .build();
        QuizQuestion questionToUpdate2 = QuizQuestion.newBuilder()
                .id(section.getLastQuestionId())
                .question(newText)
                .build();
        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(questionToUpdate1, questionToUpdate2);

        httpPutList(QUESTIONS_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> response = httpGetList(QUESTIONS_API + "?filterSectionId=" + section.getId(), QUESTIONS_TYPE);
        List<QuizQuestion> questions = response.getBody();

        for (QuizQuestion question : questions) {
            assertThat("Updated question text is " + newText, question.getQuestion(), equalTo(newText));
        }
        assertThat("Return 2 questions", questions, hasSize(2));
    }

    //TODO: other endpoints

}