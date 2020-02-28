package Insufodo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private Integer year;

    @ManyToMany
    @JoinTable(name = "correlative", joinColumns = {@JoinColumn(name = "subject_id")}, inverseJoinColumns = {@JoinColumn(name = "correlative_id")})
    private List<Subject> correlatives;

    @ManyToMany(mappedBy = "correlatives")
    private List<Subject> subjects;

    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private List<Cohort> cohorts;
}