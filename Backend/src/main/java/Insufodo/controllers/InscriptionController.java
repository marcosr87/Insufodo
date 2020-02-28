package Insufodo.controllers;

import Insufodo.models.CohortDTO;
import Insufodo.models.Inscription;
import Insufodo.models.InscriptionDTO;
import Insufodo.models.StudentDTO;
import Insufodo.services.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/inscription")
@CrossOrigin(origins = "*")
public class InscriptionController {

    @Autowired
    private InscriptionService is;

    @PostMapping("")
    public ResponseEntity addInscription(@RequestBody final @NotNull Inscription i) {
        return is.addInscription(i);
    }

    @GetMapping("")
    public List<InscriptionDTO> getAll() {
        return is.getAll();
    }

    @GetMapping("/{id}")
    public InscriptionDTO getInscription(@PathVariable final @NotNull Integer id) {
        return is.getInscription(id);
    }

    @GetMapping("/{id}/cohort")
    public CohortDTO getInscriptionCohort(@PathVariable final @NotNull Integer id) {
        return is.getInscriptionCohort(id);
    }

    @GetMapping("/{id}/student")
    public StudentDTO getInscriptionStudent(@PathVariable final @NotNull Integer id) {
        return is.getInscriptionStudent(id);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity updateInscription(@PathVariable final @NotNull Integer id, @RequestBody final @NotNull Inscription i) {
        return is.updateInscription(id, i);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity deleteInscription(@PathVariable final @NotNull Integer id) {
        return is.deleteInscription(id);
    }
}