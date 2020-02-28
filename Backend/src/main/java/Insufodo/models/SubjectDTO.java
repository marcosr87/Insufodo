package Insufodo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class SubjectDTO {

    @Id
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private Integer year;
}