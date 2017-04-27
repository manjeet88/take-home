package es.nitaur;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="QUIZ")
public class Quiz extends GenericEntity {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="quiz_fk")
    @Fetch(FetchMode.SUBSELECT)
    private List<QuizSection> sections;

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
}