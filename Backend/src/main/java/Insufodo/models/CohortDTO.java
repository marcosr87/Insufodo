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
public class CohortDTO {

    @Id
    private Integer id;
    @NotNull
    private String type;
    @NotNull
    private Integer quote;
    @NotNull
    private Integer year;
    @NotNull
    private String date;
    @NotNull
    private String weekday;
    @NotNull
    private Integer schedule;
}