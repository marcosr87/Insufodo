package Insufodo.services;

import Insufodo.models.Student;
import Insufodo.models.User;
import Insufodo.repositories.StudentRepository;
import Insufodo.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class UserService {

    @Autowired
    public UserService(UserRepository ur, StudentRepository sr) {
        this.ur = ur;
        this.sr = sr;
    }

    private final UserRepository ur;
    private final StudentRepository sr;

    public ResponseEntity addUser(User u) {
        try {
            ur.save(u);
            return ResponseEntity.status(CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public String login(User u) {
        User us = new User();
        if (ur.findById(u.getName()).isPresent()) {
            us = ur.findById(u.getName()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "usuario no encontrado"));
        } else {
            Student st = (Student) Arrays.stream(sr.findAll().toArray()).filter(s -> ((Student) s).getEmail().equals(u.getName())).filter(s -> ((Student) s).getStatus().equals("Activo")).findFirst().orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "usuario no encontrado"));
            us.setName(st.getEmail());
            us.setPassword(st.getDni());
        }
        if (us.getPassword().equals(u.getPassword())) {
            return getJWTToken(us.getName());
        }
        return "ERROR";
    }

    public ResponseEntity identity(String email) {
        if (ur.existsById(email)) {
            return ResponseEntity.status(CONFLICT).build();
        } else {
            return Arrays.stream(sr.findAll().toArray()).filter(s -> ((Student) s).getEmail().equals(email)).anyMatch(Objects::nonNull) ? ResponseEntity.status(CONFLICT).build() : ResponseEntity.noContent().build();
        }
    }

    public ResponseEntity userManual(String fileName, HttpServletResponse res) throws Exception {
        try {
            res.setHeader("Content-Disposition", "attachment; fileName=" + fileName);
            res.getOutputStream().write(contentOf(fileName));
            return ResponseEntity.status(OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts.builder().setId("JWT").setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        return token;
    }

    private byte[] contentOf(String fileName) throws Exception {
        return Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
    }
}