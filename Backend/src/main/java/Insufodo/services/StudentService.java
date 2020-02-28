package Insufodo.services;

import Insufodo.models.Inscription;
import Insufodo.models.InscriptionDTO;
import Insufodo.models.Student;
import Insufodo.repositories.StudentRepository;
import Insufodo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class StudentService {

    @Autowired
    public StudentService(StudentRepository sr, UserRepository ur) {
        this.sr = sr;
        this.ur = ur;
    }

    private final StudentRepository sr;
    private final UserRepository ur;
    private final ModelMapper mm = new ModelMapper();

    public ResponseEntity addStudent(Student s) {
        try {
            sr.save(s);
            return ResponseEntity.status(CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<Student> getAll() {
        return sr.findAll();
    }

    public List<Student> getFromTo(Integer page, Integer size) {
        Page<Student> p = sr.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "lastName"));
        List<Student> students = new ArrayList<>();
        for (Student s : p) {
            students.add(s);
        }
        return students;
    }

    public Integer getTotal() {
        return sr.findAll().size();
    }

    public ResponseEntity updateStudent(Integer id, Student s) {
        try {
            Student st = sr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "estudiante no encontrado"));
            st.setDni(s.getDni());
            st.setLastName(s.getLastName());
            st.setFirstName(s.getFirstName());
            st.setEmail(s.getEmail());
            st.setCohort(s.getCohort());
            st.setStatus(s.getStatus());
            st.setGender(s.getGender());
            st.setAddress(s.getAddress());
            st.setPhone(s.getPhone());
            sr.save(st);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity deleteStudent(Integer id) {
        try {
            sr.deleteById(id);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public Student getStudent(Integer id) {
        return sr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "estudiante no encontrado"));
    }

    public Student getByDni(String dni) {
        return (Student) Arrays.stream(sr.findAll().toArray()).filter(s -> ((Student) s).getDni().equals(dni)).findFirst().orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "estudiante no encontrado"));
    }

    public List<InscriptionDTO> getStudentInscriptions(Integer id) {
        Student s = sr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "estudiante no encontrado"));
        List<InscriptionDTO> inscriptions = new ArrayList<>();
        for (Inscription i : s.getInscriptions()) {
            inscriptions.add(mm.map(i, InscriptionDTO.class));
        }
        return inscriptions;
    }

    public ResponseEntity identityDNI(String dni) {
        return Arrays.stream(sr.findAll().toArray()).filter(s -> ((Student) s).getDni().equals(dni)).anyMatch(Objects::nonNull) ? ResponseEntity.status(CONFLICT).build() : ResponseEntity.noContent().build();
    }

    public ResponseEntity identityEmail(String email) {
        if (Arrays.stream(sr.findAll().toArray()).filter(s -> ((Student) s).getEmail().equals(email)).anyMatch(Objects::nonNull)) {
            return ResponseEntity.status(CONFLICT).build();
        } else {
            return ur.existsById(email) ? ResponseEntity.status(CONFLICT).build() : ResponseEntity.noContent().build();
        }
    }

    public ResponseEntity studentManual(String fileName, HttpServletResponse res) throws Exception {
        try {
            res.setHeader("Content-Disposition", "attachment; fileName=" + fileName);
            res.getOutputStream().write(contentOf(fileName));
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] contentOf(String fileName) throws Exception {
        return Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
    }
}