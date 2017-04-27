package es.nitaur;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultipleUsersTest {

    public static final String GET_QUESTION_API = "/api/quiz/getQuestion/1";

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void answerQuestions() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            Runnable runnable = new HttpPostRunnable(port, i);
            executorService.submit(runnable);
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        QuizQuestion question = restTemplate.getForObject(GET_QUESTION_API, QuizQuestion.class);
        assertThat("There were 10 updates to the question", 10L, is(question.getUpdateCount()));
    }

}
