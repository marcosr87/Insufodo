package Insufodo.controllers;

import Insufodo.InsufodoApplication;
import Insufodo.models.Inscription;
import Insufodo.models.InscriptionDTO;
import Insufodo.models.Student;
import Insufodo.services.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsufodoApplication.class)
public class StudentControllerTest {

    @Mock
    private StudentService ss;

    @InjectMocks
    private StudentController sc;

    @Test
    public void addStudent() {
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        when(sc.addStudent(s)).thenReturn(ResponseEntity.status(CREATED).build());
        ResponseEntity response = sc.addStudent(s);
        assertEquals(response.getStatusCode(), CREATED);
    }

    @Test
    public void getAll() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null));
        studentList.add(new Student(1, "87654321", "ggg", "hhh", "w@w", 2020, "iii", "jjj", "kkk", "lll", null));
        when(sc.getAll()).thenReturn(studentList);
        List<Student> sl = sc.getAll();
        assertEquals(((List<Student>) sl).size(), studentList.size());
    }

    @Test
    public void getFromTo() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null));
        studentList.add(new Student(1, "87654321", "ggg", "hhh", "w@w", 2020, "iii", "jjj", "kkk", "lll", null));
        when(sc.getFromTo(0, 2)).thenReturn(studentList);
        List<Student> sl = sc.getFromTo(0, 2);
        assertEquals(((List<Student>) sl).size(), studentList.size());
    }

    @Test
    public void getTotal() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null));
        studentList.add(new Student(1, "87654321", "ggg", "hhh", "w@w", 2020, "iii", "jjj", "kkk", "lll", null));
        when(sc.getTotal()).thenReturn(studentList.size());
        Integer total = sc.getTotal();
        assertEquals(total, Integer.valueOf(studentList.size()));
    }

    @Test
    public void updateStudent() {
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        when(sc.updateStudent(0, s)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = sc.updateStudent(0, s);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void deleteStudent() {
        when(sc.deleteStudent(0)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = sc.deleteStudent(0);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void getStudent() {
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        when(sc.getStudent(0)).thenReturn(s);
        Student st = sc.getStudent(0);
        assertEquals(st.getClass(), Student.class);
    }

    @Test
    public void getByDni() {
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        when(sc.getByDni("12345678")).thenReturn(s);
        Student st = sc.getByDni("12345678");
        assertEquals(st.getClass(), Student.class);
    }

    @Test
    public void getStudentInscriptions() {
        List<Inscription> il = new ArrayList<>();
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", il);
        List<InscriptionDTO> inscriptionsList = new ArrayList<>();
        ModelMapper mm = new ModelMapper();
        for (Inscription i : s.getInscriptions()) {
            inscriptionsList.add(mm.map(i, InscriptionDTO.class));
        }
        when(sc.getStudentInscriptions(0)).thenReturn(inscriptionsList);
        List<InscriptionDTO> inscriptions = sc.getStudentInscriptions(0);
        assertEquals(((List<InscriptionDTO>) inscriptions).size(), inscriptionsList.size());
    }

    @Test
    public void identityDNI() {
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        when(sc.identityDNI("12345678")).thenReturn(ResponseEntity.status(CONFLICT).build());
        ResponseEntity response = sc.identityDNI("12345678");
        assertEquals(response.getStatusCode(), CONFLICT);
    }

    @Test
    public void identityEmail() {
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        when(sc.identityEmail("q@q")).thenReturn(ResponseEntity.status(CONFLICT).build());
        ResponseEntity response = sc.identityEmail("q@q");
        assertEquals(response.getStatusCode(), CONFLICT);
    }
}