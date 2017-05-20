package es.nitaur.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "QUIZ_QUESTION")
public class QuizQuestion extends GenericEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String question;

    @Column(name = "update_count")
    private Long updateCount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizAnswer> answers;

    @ManyToOne
    @JoinColumn(name = "section_fk")
    private QuizSection section;

    public QuizQuestion() {
    }

    private QuizQuestion(Builder builder) {
        setId(builder.id);
        setQuestion(builder.question);
        setUpdateCount(builder.updateCount);
        setAnswers(builder.answers);
        setSection(builder.section);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<QuizAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswer> answers) {
        this.answers = answers;
    }

    @JsonIgnore
    public QuizSection getSection() {
        return section;
    }

    public void setSection(QuizSection section) {
        this.section = section;
    }

    public Long getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Long updateCount) {
        this.updateCount = updateCount;
    }

    public static final class Builder {
        private Long id;
        private String question;
        private Long updateCount;
        private List<QuizAnswer> answers;
        private QuizSection section;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder question(String question) {
            this.question = question;
            return this;
        }

        public Builder updateCount(Long updateCount) {
            this.updateCount = updateCount;
            return this;
        }

        public Builder answers(List<QuizAnswer> answers) {
            this.answers = answers;
            return this;
        }

        public Builder section(QuizSection section) {
            this.section = section;
            return this;
        }

        public QuizQuestion build() {
            return new QuizQuestion(this);
        }
    }
}
