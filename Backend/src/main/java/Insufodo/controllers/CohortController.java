package Insufodo.controllers;

import Insufodo.models.Cohort;
import Insufodo.models.CohortDTO;
import Insufodo.models.InscriptionDTO;
import Insufodo.models.SubjectDTO;
import Insufodo.services.CohortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/cohort")
@CrossOrigin(origins = "*")
public class CohortController {

    @Autowired
    private CohortService cs;

    @PostMapping("")
    public ResponseEntity addCohort(@RequestBody final @NotNull Cohort c) {
        return cs.addCohort(c);
    }

    @GetMapping("/getAll")
    public List<CohortDTO> getAll() {
        return cs.getAll();
    }

    @GetMapping("")
    public List<CohortDTO> getFromTo(@RequestParam final @NotNull Integer page, @RequestParam final @NotNull Integer size) {
        return cs.getFromTo(page, size);
    }

    @GetMapping("/total")
    public Integer getTotal() {
        return cs.getTotal();
    }

    @GetMapping("/{id}/subject")
    public SubjectDTO getCohortSubject(@PathVariable final @NotNull Integer id) {
        return cs.getCohortSubject(id);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity updateCohort(@PathVariable final @NotNull Integer id, @RequestBody final @NotNull Cohort c) {
        return cs.updateCohort(id, c);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity deleteCohort(@PathVariable final @NotNull Integer id) {
        return cs.deleteCohort(id);
    }

    @GetMapping("/{id}")
    public CohortDTO getCohort(@PathVariable final @NotNull Integer id) {
        return cs.getCohort(id);
    }

    @GetMapping("/{id}/inscriptions")
    public List<InscriptionDTO> getCohortInscriptions(@PathVariable final @NotNull Integer id) {
        return cs.getCohortInscriptions(id);
    }
}