package es.nitaur.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="QUIZ_ANSWER")
public class QuizAnswer extends GenericEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String answer;

    @ManyToOne
    @JoinColumn(name="question_fk")
    private QuizQuestion question;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @JsonIgnore
    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

}
