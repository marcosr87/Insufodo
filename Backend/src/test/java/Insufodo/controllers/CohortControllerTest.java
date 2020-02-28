package Insufodo.controllers;

import Insufodo.InsufodoApplication;
import Insufodo.models.Cohort;
import Insufodo.models.CohortDTO;
import Insufodo.models.Inscription;
import Insufodo.models.InscriptionDTO;
import Insufodo.models.Student;
import Insufodo.models.Subject;
import Insufodo.models.SubjectDTO;
import Insufodo.services.CohortService;
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
public class CohortControllerTest {

    @Mock
    private CohortService cs;

    @InjectMocks
    private CohortController cc;

    @Test
    public void addCohort() {
        Cohort c = new Cohort(0, null, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8, null);
        when(cc.addCohort(c)).thenReturn(ResponseEntity.status(CREATED).build());
        ResponseEntity response = cc.addCohort(c);
        assertEquals(response.getStatusCode(), CREATED);
    }

    @Test
    public void getAll() {
        List<CohortDTO> cohortList = new ArrayList<>();
        cohortList.add(new CohortDTO(0, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8));
        cohortList.add(new CohortDTO(1, "Final", 99, 2020, "31/12/2020", "Lunes", 8));
        when(cc.getAll()).thenReturn(cohortList);
        List<CohortDTO> cl = cc.getAll();
        assertEquals(((List<CohortDTO>) cl).size(), cohortList.size());
    }

    @Test
    public void getFromTo() {
        List<CohortDTO> cohortList = new ArrayList<>();
        cohortList.add(new CohortDTO(0, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8));
        cohortList.add(new CohortDTO(1, "Final", 99, 2020, "31/12/2020", "Lunes", 8));
        when(cc.getFromTo(0, 2)).thenReturn(cohortList);
        List<CohortDTO> cl = cc.getFromTo(0, 2);
        assertEquals(((List<CohortDTO>) cl).size(), cohortList.size());
    }

    @Test
    public void getTotal() {
        List<Cohort> cohortList = new ArrayList<>();
        cohortList.add(new Cohort(0, null, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8, null));
        cohortList.add(new Cohort(1, null, "Final", 99, 2020, "31/12/2020", "Lunes", 8, null));
        when(cc.getTotal()).thenReturn(cohortList.size());
        Integer total = cc.getTotal();
        assertEquals(total, Integer.valueOf(cohortList.size()));
    }

    @Test
    public void getCohortSubject() {
        Subject s = new Subject(0, "Fisica", 2020, null, null, null);
        Cohort c = new Cohort(0, s, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8, null);
        ModelMapper mm = new ModelMapper();
        when(cc.getCohortSubject(0)).thenReturn(mm.map(c.getSubject(), SubjectDTO.class));
        SubjectDTO st = cc.getCohortSubject(0);
        assertEquals(st.getClass(), SubjectDTO.class);
    }

    @Test
    public void updateCohort() {
        Cohort c = new Cohort(0, null, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8, null);
        when(cc.updateCohort(0, c)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = cc.updateCohort(0, c);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void deleteCohort() {
        when(cc.deleteCohort(0)).thenReturn(ResponseEntity.status(OK).build());
        ResponseEntity response = cc.deleteCohort(0);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void getCohort() {
        CohortDTO c = new CohortDTO(0, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8);
        when(cc.getCohort(0)).thenReturn(c);
        CohortDTO co = cc.getCohort(0);
        assertEquals(co.getClass(), CohortDTO.class);
    }

    @Test
    public void getCohortInscriptions() {
        Subject s = new Subject(0, "Fisica", 2020, null, null, null);
        Cohort c = new Cohort(0, s, "Cursada", 99, 2020, "31/12/2020", "Lunes", 8, null);
        Student st = new Student(0, "12345678", "aaa", "bbb", "q@q", 2020, "ccc", "ddd", "eee", "fff", null);
        Inscription i = new Inscription(0, st, c, "31/12/2020", 10);
        List<Inscription> il = new ArrayList<>();
        il.add(i);
        c.setInscriptions(il);
        List<InscriptionDTO> inscriptions = new ArrayList<>();
        ModelMapper mm = new ModelMapper();
        for (Inscription in : c.getInscriptions()) {
            inscriptions.add(mm.map(in, InscriptionDTO.class));
        }
        when(cc.getCohortInscriptions(0)).thenReturn(inscriptions);
        List<InscriptionDTO> inscriptionsList = cc.getCohortInscriptions(0);
        assertEquals(((List<InscriptionDTO>) inscriptionsList).size(), inscriptions.size());
    }
}