package es.nitaur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="QUIZ_QUESTION")
public class QuizQuestion extends GenericEntity {

    private static final long serialVersionUID = 1L;

    private String question;

    @Column(name = "update_count")
    private Long updateCount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="question_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizAnswer> answers;

    @ManyToOne
    @JoinColumn(name="section_fk")
    private QuizSection section;

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
}
