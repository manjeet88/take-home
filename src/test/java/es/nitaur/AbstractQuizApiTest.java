package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import es.nitaur.repository.QuizRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractQuizApiTest extends AbstractRestTest {
    public static final String QUIZZES_API = "/api/quizzes";
    public static final String QUESTIONS_API = "/api/questions";

    public static final ParameterizedTypeReference<List<Quiz>> QUIZZES_TYPE = new ParameterizedTypeReference<List<Quiz>>() {
    };
    public static final ParameterizedTypeReference<List<QuizQuestion>> QUESTIONS_TYPE = new ParameterizedTypeReference<List<QuizQuestion>>() {
    };

    @Autowired
    private QuizRepository quizRepository;

    @Before
    public void setUp() {
        cleanUp();
    }

    protected void cleanUp() {
        quizRepository.deleteAll();
    }

    protected Quiz saveQuize1() {
        final Quiz quiz = buildQuize("Quiz 1", newArrayList(newArrayList("Q11", "Q12"), newArrayList("Q13", "Q14")));
        final Quiz savedQuiz = quizRepository.save(quiz);
        return savedQuiz;
    }

    protected Quiz saveQuize2() {
        final Quiz quiz = buildQuize("Quiz 2", newArrayList(newArrayList("Q21", "Q22"), newArrayList("Q23", "Q24")));
        final Quiz savedQuiz = quizRepository.save(quiz);
        return savedQuiz;
    }

    protected Quiz buildQuize() {
        return buildQuize("Quiz 0", newArrayList(newArrayList("Q01", "Q02"), newArrayList("Q03", "Q04")));
    }

    protected Quiz buildQuize(String quizeName, List<List<String>> questionSections) {
        Quiz quiz = new Quiz();
        quiz.setName(quizeName);
        quiz.setSections(Lists.newArrayList());

        for (List<String> questionSection : questionSections) {
            QuizSection section = new QuizSection();
            section.setQuizQuestions(Lists.newArrayList());
            for (String questionText : questionSection) {
                QuizQuestion question = new QuizQuestion();
                question.setQuestion(questionText);
                question.setUpdateCount(0L);
                section.getQuizQuestions().add(question);
            }
            quiz.getSections().add(section);
        }
        return quiz;
    }
}