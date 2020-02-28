package Insufodo.controllers;

import Insufodo.models.Student;
import Insufodo.models.InscriptionDTO;
import Insufodo.services.StudentService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService ss;

    @PostMapping("")
    public ResponseEntity addStudent(@RequestBody final @NotNull Student s) {
        return ss.addStudent(s);
    }

    @GetMapping("/getAll")
    public List<Student> getAll() {
        return ss.getAll();
    }

    @GetMapping("")
    public List<Student> getFromTo(@RequestParam final @NotNull Integer page, @RequestParam final @NotNull Integer size) {
        return ss.getFromTo(page, size);
    }

    @GetMapping("/total")
    public Integer getTotal() {
        return ss.getTotal();
    }

    @PostMapping("/{id}/update")
    public ResponseEntity updateStudent(@PathVariable final @NotNull Integer id, @RequestBody final @NotNull Student s) {
        return ss.updateStudent(id, s);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity deleteStudent(@PathVariable final @NotNull Integer id) {
        return ss.deleteStudent(id);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable final @NotNull Integer id) {
        return ss.getStudent(id);
    }

    @GetMapping("/dni/{dni}")
    public Student getByDni(@PathVariable final @NotNull String dni) {
        return ss.getByDni(dni);
    }

    @GetMapping("/{id}/inscriptions")
    public List<InscriptionDTO> getStudentInscriptions(@PathVariable final @NotNull Integer id) {
        return ss.getStudentInscriptions(id);
    }

    @GetMapping("/identityDNI")
    @ApiResponses({
            @ApiResponse(code = 204, message = "dni no existente"),
            @ApiResponse(code = 409, message = "dni ya existente")
    })
    public ResponseEntity identityDNI(@RequestParam final @NotNull String dni) {
        return ss.identityDNI(dni);
    }

    @GetMapping("/identityEmail")
    @ApiResponses({
            @ApiResponse(code = 204, message = "email no existente"),
            @ApiResponse(code = 409, message = "email ya existente")
    })
    public ResponseEntity identityEmail(@RequestParam final @NotNull String email) {
        return ss.identityEmail(email);
    }

    @GetMapping("/studentManual")
    public ResponseEntity studentManual(String fileName, HttpServletResponse res) throws Exception {
        return ss.studentManual(fileName, res);
    }
}