package Insufodo.controllers;

import Insufodo.models.Subject;
import Insufodo.models.SubjectDTO;
import Insufodo.services.SubjectService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/subject")
@CrossOrigin(origins = "*")
public class SubjectController {

    @Autowired
    private SubjectService ss;

    @PostMapping("")
    public ResponseEntity addSubject(@RequestBody final @NotNull Subject s) {
        return ss.addSubject(s);
    }

    @GetMapping("/getAll")
    public List<SubjectDTO> getAll() {
        return ss.getAll();
    }

    @GetMapping("")
    public List<SubjectDTO> getFromTo(@RequestParam final @NotNull Integer page, @RequestParam final @NotNull Integer size) {
        return ss.getFromTo(page, size);
    }

    @GetMapping("/total")
    public Integer getTotal() {
        return ss.getTotal();
    }

    @PostMapping("/{id}/update")
    public ResponseEntity updateSubject(@PathVariable final @NotNull Integer id, @RequestBody final @NotNull Subject s) {
        return ss.updateSubject(id, s);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity deleteSubject(@PathVariable final @NotNull Integer id) {
        return ss.deleteSubject(id);
    }

    @GetMapping("/{id}")
    public SubjectDTO getSubject(@PathVariable final @NotNull Integer id) {
        return ss.getSubject(id);
    }

    @GetMapping("/{id}/correlatives")
    public List<SubjectDTO> getSubjectCorrelatives(@PathVariable final @NotNull Integer id) {
        return ss.getSubjectCorrelatives(id);
    }

    @GetMapping("/identity")
    @ApiResponses({
            @ApiResponse(code = 204, message = "materia no existente"),
            @ApiResponse(code = 409, message = "materia ya existente")
    })
    public ResponseEntity identity(@RequestParam final @NotNull String name) {
        return ss.identity(name);
    }
}