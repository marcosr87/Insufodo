package Insufodo.services;

import Insufodo.models.Cohort;
import Insufodo.models.CohortDTO;
import Insufodo.models.SubjectDTO;
import Insufodo.models.Inscription;
import Insufodo.models.InscriptionDTO;
import Insufodo.repositories.CohortRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class CohortService {

    @Autowired
    private CohortRepository cr;

    private final ModelMapper mm = new ModelMapper();

    public ResponseEntity addCohort(Cohort c) {
        try {
            cr.save(c);
            return ResponseEntity.status(CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<CohortDTO> getAll() {
        List<CohortDTO> cohorts = new ArrayList<>();
        for (Cohort c : cr.findAll()) {
            cohorts.add(mm.map(c, CohortDTO.class));
        }
        return cohorts;
    }

    public List<CohortDTO> getFromTo(Integer page, Integer size) {
        Page<Cohort> p = cr.findAll(PageRequest.of(page, size, Sort.Direction.DESC, "year"));
        List<CohortDTO> cohorts = new ArrayList<>();
        for (Cohort c : p) {
            cohorts.add(mm.map(c, CohortDTO.class));
        }
        return cohorts;
    }

    public Integer getTotal() {
        return cr.findAll().size();
    }

    public SubjectDTO getCohortSubject(Integer id) {
        return mm.map(cr.findById(id).get().getSubject(), SubjectDTO.class);
    }

    public ResponseEntity updateCohort(Integer id, Cohort c) {
        try {
            Cohort co = cr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "cohorte no encontrada"));
            co.setSubject(c.getSubject());
            co.setType(c.getType());
            co.setQuote(c.getQuote());
            co.setYear(c.getYear());
            co.setDate(c.getDate());
            co.setWeekday(c.getWeekday());
            co.setSchedule(c.getSchedule());
            cr.save(co);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity deleteCohort(Integer id) {
        try {
            Cohort c = cr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "cohorte no encontrada"));
            c.getInscriptions().clear();
            cr.save(c);
            cr.deleteById(id);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public CohortDTO getCohort(Integer id) {
        Cohort c = cr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "cohorte no encontrada"));
        return mm.map(c, CohortDTO.class);
    }

    public List<InscriptionDTO> getCohortInscriptions(Integer id) {
        Cohort c = cr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "cohorte no encontrada"));
        List<InscriptionDTO> inscriptions = new ArrayList<>();
        for (Inscription i : c.getInscriptions()) {
            inscriptions.add(mm.map(i, InscriptionDTO.class));
        }
        return inscriptions;
    }
}