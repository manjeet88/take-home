package es.nitaur;

import es.nitaur.domain.QuizQuestion;
import org.junit.Test;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.http.ResponseEntity;

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
        QuizQuestion savedQuestion = saveQuizWithQuestion("Check answers?");
        Long questionId = savedQuestion.getId();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        String answers = "[{\"answer\":\"Test @idx@\"}, {\"answer\": \"TEST @idx@\"}]";
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new HttpPostRunnable(port, i, QUESTIONS_API + "/" + questionId + "/answers", answers);
            executorService.submit(runnable);
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        ResponseEntity<QuizQuestion> response = httpGetOne(QUESTIONS_API + "/" + questionId, QuizQuestion.class);
        QuizQuestion question = response.getBody();

        assertThat("There were 10 updates to the question", question.getUpdateCount(), is(10L));
    }

}
