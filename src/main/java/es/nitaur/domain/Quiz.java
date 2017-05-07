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