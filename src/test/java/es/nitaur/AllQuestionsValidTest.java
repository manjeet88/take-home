package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;

public class AllQuestionsValidTest extends AbstractQuizApiTest {

    @Test
    public void questionsAreNotSavedWithEmptyQuestionText() throws Exception {
        Quiz savedQuiz = saveQuize1();
        QuizSection savedSection = savedQuiz.getSections().iterator().next();
        Iterator<QuizQuestion> iterator = savedSection.getQuizQuestions().iterator();
        QuizQuestion savedQuestion1 = iterator.next();
        QuizQuestion savedQuestion2 = iterator.next();

        QuizQuestion quizQuestion1 = new QuizQuestion();
        quizQuestion1.setId(savedQuestion1.getId());
        quizQuestion1.setQuestion("<<redacted>>");
        QuizQuestion quizQuestion2 = new QuizQuestion();
        quizQuestion2.setId(savedQuestion2.getId());
        quizQuestion2.setQuestion(null);

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quizQuestion1, quizQuestion2);

        httpPutList(QUESTIONS_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> response = httpGetList(QUESTIONS_API + "?filterSectionId=" + savedSection.getId(), QUESTIONS_TYPE);
        List<QuizQuestion> questions = response.getBody();

        for (QuizQuestion question : questions) {
            assertThat("Question text should not be <<redacted>>", question.getQuestion(), not("<<redacted>>"));
        }
        assertThat("Return 2 questions", questions, hasSize(2));
    }

    @Test
    public void questionsAreSavedWithQuestionText() throws Exception {
        Quiz savedQuiz = saveQuize1();
        QuizSection savedSection = savedQuiz.getSections().iterator().next();
        Iterator<QuizQuestion> iterator = savedSection.getQuizQuestions().iterator();
        QuizQuestion savedQuestion1 = iterator.next();
        QuizQuestion savedQuestion2 = iterator.next();

        QuizQuestion quizQuestion1 = new QuizQuestion();
        quizQuestion1.setId(savedQuestion1.getId());
        quizQuestion1.setQuestion("<<redacted>>");
        QuizQuestion quizQuestion2 = new QuizQuestion();
        quizQuestion2.setId(savedQuestion2.getId());
        quizQuestion2.setQuestion("<<redacted>>");

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quizQuestion1, quizQuestion2);

        httpPutList(QUESTIONS_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> response = httpGetList(QUESTIONS_API + "?filterSectionId=" + savedSection.getId(), QUESTIONS_TYPE);
        List<QuizQuestion> questions = response.getBody();

        for (QuizQuestion quizQuestion : questions) {
            assertThat("Question text is only <<redacted>>", quizQuestion.getQuestion(), is("<<redacted>>"));
        }
        assertThat("Return 2 questions", questions, hasSize(2));
    }
}
