package Insufodo.services;

import Insufodo.models.Subject;
import Insufodo.models.SubjectDTO;
import Insufodo.repositories.SubjectRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository sr;

    private final ModelMapper mm = new ModelMapper();

    public ResponseEntity addSubject(Subject s) {
        try {
            sr.save(s);
            return ResponseEntity.status(CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<SubjectDTO> getAll() {
        List<SubjectDTO> subjects = new ArrayList<>();
        for (Subject s : sr.findAll()) {
            subjects.add(mm.map(s, SubjectDTO.class));
        }
        return subjects;
    }

    public List<SubjectDTO> getFromTo(Integer page, Integer size) {
        Page<Subject> p = sr.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "name"));
        List<SubjectDTO> subjects = new ArrayList<>();
        for (Subject s : p) {
            subjects.add(mm.map(s, SubjectDTO.class));
        }
        return subjects;
    }

    public Integer getTotal() {
        return sr.findAll().size();
    }

    public ResponseEntity updateSubject(Integer id, Subject s) {
        try {
            Subject su = sr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "materia no encontrada"));
            su.setName(s.getName());
            su.setYear(s.getYear());
            su.setCorrelatives(s.getCorrelatives());
            sr.save(su);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity deleteSubject(Integer id) {
        try {
            Subject s = sr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "materia no encontrada"));
            s.getCorrelatives().clear();
            sr.save(s);
            sr.deleteById(id);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public SubjectDTO getSubject(Integer id) {
        Subject s = sr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "materia no encontrada"));
        return mm.map(s, SubjectDTO.class);
    }

    public List<SubjectDTO> getSubjectCorrelatives(Integer id) {
        Subject s = sr.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "materia no encontrada"));
        List<SubjectDTO> correlatives = new ArrayList<>();
        for (Subject c : s.getCorrelatives()) {
            correlatives.add(mm.map(c, SubjectDTO.class));
        }
        return correlatives;
    }

    public ResponseEntity identity(String name) {
        return Arrays.stream(sr.findAll().toArray()).filter(s -> ((Subject) s).getName().equals(name)).anyMatch(Objects::nonNull) ? ResponseEntity.status(CONFLICT).build() : ResponseEntity.noContent().build();
    }
}