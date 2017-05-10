package es.nitaur.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "QUIZ_SECTION")
public class QuizSection extends GenericEntity {

    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "section_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizQuestion> quizQuestions;

    @ManyToOne
    @JoinColumn(name = "quiz_fk")
    private Quiz quiz;

    public QuizSection() {
    }

    private QuizSection(Builder builder) {
        setQuizQuestions(builder.quizQuestions);
        setQuiz(builder.quiz);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    @JsonIgnore
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Long getFirstQuestionId() {
        return quizQuestions == null || quizQuestions.isEmpty() ? null : quizQuestions.iterator().next().getId();
    }

    public Long getLastQuestionId() {
        return quizQuestions == null || quizQuestions.isEmpty() ? null : quizQuestions.get(quizQuestions.size() - 1).getId();
    }

    public static final class Builder {
        private List<QuizQuestion> quizQuestions;
        private Quiz quiz;

        private Builder() {
        }

        public Builder quizQuestions(List<QuizQuestion> quizQuestions) {
            this.quizQuestions = quizQuestions;
            return this;
        }

        public Builder quiz(Quiz quiz) {
            this.quiz = quiz;
            return this;
        }

        public QuizSection build() {
            return new QuizSection(this);
        }
    }
}
