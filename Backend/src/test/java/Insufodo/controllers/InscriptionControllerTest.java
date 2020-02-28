package Insufodo.controllers;

import Insufodo.InsufodoApplication;
import Insufodo.models.Cohort;
import Insufodo.models.CohortDTO;
import Insufodo.models.Inscription;
import Insufodo.models.InscriptionDTO;
import Insufodo.models.Student;
import Insufodo.models.StudentDTO;
import Insufodo.services.InscriptionService;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsufodoApplication.class)
public class InscriptionControllerTest {

    @Mock
    private InscriptionService is;

    @InjectMocks
    private InscriptionController ic;

    @Test
    public void addInscription() {
        Inscription i = new Inscription(0, null, null, "31/12/2020", 10);
        when(ic.addInscription(i)).thenReturn(ResponseEntity.status(CREATED).build());
        ResponseEntity response = ic.addInscription(i);
        assertEquals(response.getStatusCode(), CREATED);
    }

    @Test
    public void getAll() {
        List<InscriptionDTO> inscriptionList = new ArrayList<>();
        inscriptionList.add(new InscriptionDTO(0, "31/12/2020", 10));
        inscriptionList.add(new InscriptionDTO(1, "31/12/2020", 10));
        when(ic.getAll()).thenReturn(inscriptionList);
        List<InscriptionDTO> sl = ic.getAll();
        assertEquals(((List<InscriptionDTO>) sl).size(), inscriptionList.size());
    }

    @Test
    public void getInscription() {
        InscriptionDTO i = new InscriptionDTO(0, "31/12/2020", 10);
        when(ic.getInscription(0)).thenReturn(i);
        InscriptionDTO in = ic.getInscription(0);
        assertEquals(in.getClass(), InscriptionDTO.class);
    }

    @Test
    public void getInscriptionCohort() {
        Cohort c = new Cohort(0, null, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8, null);
        Inscription i = new Inscription(0, null, c, "31/12/2020", 10);
        ModelMapper mm = new ModelMapper();
        when(ic.getInscriptionCohort(0)).thenReturn(mm.map(i.getCohort(), CohortDTO.class));
        CohortDTO co = ic.getInscriptionCohort(0);
        assertEquals(co.getClass(), CohortDTO.class);
    }

    @Test
    public void getInscriptionStudent() {
        Student s = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        Inscription i = new Inscription(0, s, null, "31/12/2020", 10);
        ModelMapper mm = new ModelMapper();
        when(ic.getInscriptionStudent(0)).thenReturn(mm.map(i.getStudent(), StudentDTO.class));
        StudentDTO st = ic.getInscriptionStudent(0);
        assertEquals(st.getClass(), StudentDTO.class);
    }

    @Test
    public void updateInscription() {
        Inscription i = new Inscription(0, null, null, "31/12/2020", 10);
        when(ic.updateInscription(0, i)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = ic.updateInscription(0, i);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void deleteInscription() {
        when(ic.deleteInscription(0)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = ic.deleteInscription(0);
        assertEquals(response.getStatusCode(), OK);
    }
}