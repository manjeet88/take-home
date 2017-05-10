package es.nitaur.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "QUIZ")
public class Quiz extends GenericEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizSection> sections;

    public Quiz() {
    }

    private Quiz(Builder builder) {
        setName(builder.name);
        setSections(builder.sections);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuizSection> getSections() {
        return sections;
    }

    public void setSections(List<QuizSection> sections) {
        this.sections = sections;
    }

    public static final class Builder {
        private String name;
        private List<QuizSection> sections;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder sections(List<QuizSection> sections) {
            this.sections = sections;
            return this;
        }

        public Builder questions(List<List<QuizQuestion>> allQuestions) {
            if (allQuestions == null) {
                this.sections = null;
                return this;
            }

            List<QuizSection> sections = allQuestions.stream()
                    .map(questions -> QuizSection.newBuilder()
                                    .quizQuestions(questions)
                                    .build()
                    )
                    .collect(Collectors.toList());

            this.sections = sections;
            return this;
        }

        public Quiz build() {
            return new Quiz(this);
        }
    }
}