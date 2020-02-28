package Insufodo.controllers;

import Insufodo.InsufodoApplication;
import Insufodo.models.Subject;
import Insufodo.models.SubjectDTO;
import Insufodo.services.SubjectService;
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
public class SubjectControllerTest {

    @Mock
    private SubjectService ss;

    @InjectMocks
    private SubjectController sc;

    @Test
    public void addSubject() {
        Subject s = new Subject(0, "Fisica", 2020, null, null, null);
        when(sc.addSubject(s)).thenReturn(ResponseEntity.status(CREATED).build());
        ResponseEntity response = sc.addSubject(s);
        assertEquals(response.getStatusCode(), CREATED);
    }

    @Test
    public void getAll() {
        List<SubjectDTO> subjectList = new ArrayList<>();
        subjectList.add(new SubjectDTO(0, "Fisica", 2020));
        subjectList.add(new SubjectDTO(1, "Matematica", 2020));
        when(sc.getAll()).thenReturn(subjectList);
        List<SubjectDTO> sl = sc.getAll();
        assertEquals(((List<SubjectDTO>) sl).size(), subjectList.size());
    }

    @Test
    public void getFromTo() {
        List<SubjectDTO> subjectList = new ArrayList<>();
        subjectList.add(new SubjectDTO(0, "Fisica", 2020));
        subjectList.add(new SubjectDTO(1, "Matematica", 2020));
        when(sc.getFromTo(0, 2)).thenReturn(subjectList);
        List<SubjectDTO> sl = sc.getFromTo(0, 2);
        assertEquals(((List<SubjectDTO>) sl).size(), subjectList.size());
    }

    @Test
    public void getTotal() {
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(new Subject(0, "Fisica", 2020, null, null, null));
        subjectList.add(new Subject(1, "Matematica", 2020, null, null, null));
        when(sc.getTotal()).thenReturn(subjectList.size());
        Integer total = sc.getTotal();
        assertEquals(total, Integer.valueOf(subjectList.size()));
    }

    @Test
    public void updateSubject() {
        Subject s = new Subject(0, "Fisica", 2020, null, null, null);
        when(sc.updateSubject(0, s)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = sc.updateSubject(0, s);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void deleteSubject() {
        when(sc.deleteSubject(0)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = sc.deleteSubject(0);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void getSubject() {
        SubjectDTO s = new SubjectDTO(0, "Fisica", 2020);
        when(sc.getSubject(0)).thenReturn(s);
        SubjectDTO su = sc.getSubject(0);
        assertEquals(su.getClass(), SubjectDTO.class);
    }

    @Test
    public void getSubjectCorrelatives() {
        Subject s1 = new Subject(0, "Fisica", 2020, null, null, null);
        Subject s2 = new Subject(1, "Matematica", 2020, null, null, null);
        List<Subject> correlatives = new ArrayList<>();
        correlatives.add(s1);
        s2.setCorrelatives(correlatives);
        List<SubjectDTO> correlativeList = new ArrayList<>();
        ModelMapper mm = new ModelMapper();
        for (Subject s : s2.getCorrelatives()) {
            correlativeList.add(mm.map(s, SubjectDTO.class));
        }
        when(sc.getSubjectCorrelatives(0)).thenReturn(correlativeList);
        List<SubjectDTO> sl = sc.getSubjectCorrelatives(0);
        assertEquals(((List<SubjectDTO>) sl).size(), correlativeList.size());
    }

    @Test
    public void identity() {
        Subject s = new Subject(0, "Fisica", 2020, null, null, null);
        when(sc.identity("Fisica")).thenReturn(ResponseEntity.status(CONFLICT).build());
        ResponseEntity response = sc.identity("Fisica");
        assertEquals(response.getStatusCode(), CONFLICT);
    }
}