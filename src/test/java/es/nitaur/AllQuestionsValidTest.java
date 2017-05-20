package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;

public class AllQuestionsValidTest extends AbstractQuizApiTest {
    private static final String TEXT_REDACTED = "<<redacted>>";

    @Test
    public void questionsAreNotSavedWithEmptyQuestionText() throws Exception {
        QuizSection section = saveQuizWithSection("Value 1?", "Value 2?");

        QuizQuestion quizQuestion1 = QuizQuestion.newBuilder()
                .id(section.getFirstQuestionId())
                .question(TEXT_REDACTED)
                .build();
        QuizQuestion quizQuestion2 = QuizQuestion.newBuilder()
                .id(section.getLastQuestionId())
                .question(null)
                .build();

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quizQuestion1, quizQuestion2);

        httpPutList(QUESTIONS_API, questionsToUpdate);

        List<QuizQuestion> questions = getList(QUESTIONS_API + "?filterSectionId=" + section.getId(), QUESTIONS_TYPE);

        for (QuizQuestion question : questions) {
            assertThat("Question text should not be " + TEXT_REDACTED, question.getQuestion(), not(TEXT_REDACTED));
        }
        assertThat("Return 2 questions", questions, hasSize(2));
    }

    @Test
    public void questionsAreSavedWithQuestionText() throws Exception {
        QuizSection section = saveQuizWithSection("Value 3?", "Value 4?");

        QuizQuestion quizQuestion1 = QuizQuestion.newBuilder()
                .id(section.getFirstQuestionId())
                .question(TEXT_REDACTED)
                .build();
        QuizQuestion quizQuestion2 = QuizQuestion.newBuilder()
                .id(section.getLastQuestionId())
                .question(TEXT_REDACTED)
                .build();

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quizQuestion1, quizQuestion2);

        httpPutList(QUESTIONS_API, questionsToUpdate);

        List<QuizQuestion> questions = getList(QUESTIONS_API + "?filterSectionId=" + section.getId(), QUESTIONS_TYPE);

        for (QuizQuestion quizQuestion : questions) {
            assertThat("Question text is only " + TEXT_REDACTED, quizQuestion.getQuestion(), is(TEXT_REDACTED));
        }
        assertThat("Return 2 questions", questions, hasSize(2));
    }
}
