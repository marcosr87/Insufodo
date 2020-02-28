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
public class InscriptionDTO {

    @Id
    private Integer id;

    private String qualificationDate;

    private Integer qualification;
}