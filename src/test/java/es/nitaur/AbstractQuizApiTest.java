package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.domain.QuizSection;
import es.nitaur.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.rmi.server.UID;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractQuizApiTest extends AbstractRestTest {
    public static final String QUIZZES_API = "/api/quizzes";
    public static final String QUESTIONS_API = "/api/questions";

    public static final ParameterizedTypeReference<List<Quiz>> QUIZZES_TYPE = new ParameterizedTypeReference<List<Quiz>>() {
    };
    public static final ParameterizedTypeReference<List<QuizQuestion>> QUESTIONS_TYPE = new ParameterizedTypeReference<List<QuizQuestion>>() {
    };

    @Autowired
    private QuizRepository quizRepository;

    protected void cleanUp() {
        quizRepository.deleteAll();
    }

    protected Quiz saveQuiz() {
        Quiz quiz = buildQuiz();
        Quiz savedQuiz = quizRepository.save(quiz);
        return savedQuiz;
    }

    protected Long saveQuizWithQuestion(String questionText) {
        QuizSection section = saveQuizWithSection(questionText);
        return section.getFirstQuestionId();
    }

    protected QuizSection saveQuizWithSection(String... questionTexts) {
        Quiz quiz = buildQuiz("Any", Arrays.asList(Arrays.asList(questionTexts)));
        Quiz savedQuiz = quizRepository.save(quiz);
        return savedQuiz.getFirstSection();
    }

    protected Quiz buildQuiz() {
        return buildQuiz("Quiz", Lists.newArrayList(Lists.newArrayList("Q01", "Q02"), Lists.newArrayList("Q03", "Q04")));
    }

    private Quiz buildQuiz(String quizName, List<List<String>> allQuestions) {
        String uid = getUid();

        List<QuizSection> sections = buildSections(uid, allQuestions);

        Quiz quiz = Quiz.newBuilder()
                .name(quizName + uid)
                .sections(sections)
                .build();
        return quiz;
    }

    private List<QuizSection> buildSections(String uid, List<List<String>> allQuestions) {
        List<QuizSection> sections = allQuestions.stream()
                .map(questions -> QuizSection.newBuilder()
                                .quizQuestions(buildQuestions(uid, questions))
                                .build()
                )
                .collect(Collectors.toList());
        return sections;
    }

    private List<QuizQuestion> buildQuestions(String uid, List<String> questions) {
        List<QuizQuestion> quizQuestions = questions.stream()
                .map(questionText -> QuizQuestion.newBuilder()
                                .question(questionText + uid)
                                .updateCount(0L)
                                .build()
                )
                .collect(Collectors.toList());
        return quizQuestions;
    }

    private String getUid() {
        return new UID().toString();
    }
}