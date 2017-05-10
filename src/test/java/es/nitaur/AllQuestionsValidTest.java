package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;

public class AllQuestionsValidTest extends AbstractQuizApiTest {

    @Test
    public void questionsAreNotSavedWithEmptyQuestionText() throws Exception {
        QuizSection savedSection = saveQuizWithSection("Value 1?", "Value 2?");
        List<QuizQuestion> savedQuestions = savedSection.getQuizQuestions();
        QuizQuestion savedQuestion1 = savedQuestions.get(0);
        QuizQuestion savedQuestion2 = savedQuestions.get(1);

        QuizQuestion quizQuestion1 = QuizQuestion.newBuilder()
                .id(savedQuestion1.getId())
                .question("<<redacted>>")
                .build();
        QuizQuestion quizQuestion2 = QuizQuestion.newBuilder()
                .id(savedQuestion2.getId())
                .question(null)
                .build();

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
        QuizSection savedSection = saveQuizWithSection("Value 3?", "Value 4?");
        List<QuizQuestion> savedQuestions = savedSection.getQuizQuestions();
        QuizQuestion savedQuestion1 = savedQuestions.get(0);
        QuizQuestion savedQuestion2 = savedQuestions.get(1);

        QuizQuestion quizQuestion1 = QuizQuestion.newBuilder()
                .id(savedQuestion1.getId())
                .question("<<redacted>>")
                .build();
        QuizQuestion quizQuestion2 = QuizQuestion.newBuilder()
                .id(savedQuestion2.getId())
                .question("<<redacted>>")
                .build();

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
