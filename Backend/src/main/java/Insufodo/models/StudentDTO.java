package Insufodo.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class StudentDTO {

    @Id
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
}