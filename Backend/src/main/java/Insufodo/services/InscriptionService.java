package Insufodo.services;

import Insufodo.models.Cohort;
import Insufodo.models.CohortDTO;
import Insufodo.models.Inscription;
import Insufodo.models.InscriptionDTO;
import Insufodo.models.StudentDTO;
import Insufodo.repositories.CohortRepository;
import Insufodo.repositories.InscriptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Service
public class InscriptionService {

    @Autowired
    public InscriptionService(InscriptionRepository ir, CohortRepository cr, JavaMailSender jms) {
        this.ir = ir;
        this.cr = cr;
        this.jms = jms;
    }

    private final InscriptionRepository ir;
    private final CohortRepository cr;
    private final ModelMapper mm = new ModelMapper();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final JavaMailSender jms;

    @Value("${spring.mail.username}")
    private String from;

    public ResponseEntity addInscription(Inscription i) {
        try {
            Cohort c = cr.findById(i.getCohort().getId()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "cohorte no encontrada"));
            if (c.getQuote() > 0) {
                ir.save(i);
                c.setQuote(c.getQuote() - 1);
                cr.save(c);
                return ResponseEntity.status(CREATED).build();
            } else {
                return ResponseEntity.status(PRECONDITION_FAILED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<InscriptionDTO> getAll() {
        List<InscriptionDTO> inscriptions = new ArrayList<>();
        for (Inscription i : ir.findAll()) {
            inscriptions.add(mm.map(i, InscriptionDTO.class));
        }
        return inscriptions;
    }

    public InscriptionDTO getInscription(Integer id) {
        Inscription i = ir.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "inscripcion no encontrada"));
        return mm.map(i, InscriptionDTO.class);
    }

    public CohortDTO getInscriptionCohort(Integer id) {
        return mm.map(ir.findById(id).get().getCohort(), CohortDTO.class);
    }

    public StudentDTO getInscriptionStudent(Integer id) {
        return mm.map(ir.findById(id).get().getStudent(), StudentDTO.class);
    }

    public ResponseEntity updateInscription(Integer id, Inscription i) {
        try {
            Inscription in = ir.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "inscripcion no encontrada"));
            in.setQualification(i.getQualification());
            in.setQualificationDate(LocalDate.now().format(dtf));
            ir.save(in);
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(in.getStudent().getEmail());
            mail.setFrom(this.from);
            mail.setSubject("Calificación");
            mail.setText("Estimado/a " + in.getStudent().getLastName() + " " + in.getStudent().getFirstName() + " usted ha obtenido un " + in.getQualification() + " en " + in.getCohort().getSubject().getName() + " " + in.getCohort().getYear() + ".");
            jms.send(mail);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity deleteInscription(Integer id) {
        try {
            Inscription i = ir.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "inscripcion no encontrada"));
            Cohort c = cr.findById(i.getCohort().getId()).get();
            c.setQuote(c.getQuote() + 1);
            cr.save(c);
            ir.deleteById(id);
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}