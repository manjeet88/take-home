package es.nitaur;

import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import org.junit.Test;
import org.springframework.boot.context.embedded.LocalServerPort;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MultipleUsersTest extends AbstractQuizApiTest {

    @LocalServerPort
    int port;

    @Test
    public void answerQuestions() throws Exception {
        Quiz quiz = saveQuiz();
        QuizSection firstSection = quiz.getFirstSection();
        Long questionId = firstSection.getFirstQuestionId();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        String answers = "[{\"answer\":\"Test @idx@\"}, {\"answer\": \"TEST @idx@\"}]";
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new HttpPostRunnable(port, i, QUESTIONS_API + "/" + questionId + "/answers", answers);
            executorService.submit(runnable);
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        QuizQuestion question = getOne(QUESTIONS_API + "/" + questionId, QuizQuestion.class);

        assertThat("There were 10 updates to the question", question.getUpdateCount(), is(10L));
    }

}
