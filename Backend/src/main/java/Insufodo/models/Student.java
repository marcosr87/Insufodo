package Insufodo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String dni;
    @NotNull
    private String lastName;
    @NotNull
    private String firstName;
    @NotNull
    private String email;
    @NotNull
    private Integer cohort;
    @NotNull
    private String status;

    private String gender;

    private String address;

    private String phone;
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Inscription> inscriptions;
}