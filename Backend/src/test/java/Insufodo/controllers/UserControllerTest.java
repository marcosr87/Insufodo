package Insufodo.controllers;

import Insufodo.InsufodoApplication;
import Insufodo.models.User;
import Insufodo.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsufodoApplication.class)
public class UserControllerTest {

    @Mock
    private UserService us;

    @InjectMocks
    private UserController uc;

    @Test
    public void addUser() {
        User u = new User("aaa", "bbb");
        when(uc.addUser(u)).thenReturn(ResponseEntity.status(CREATED).build());
        ResponseEntity response = uc.addUser(u);
        assertEquals(response.getStatusCode(), CREATED);
    }

    @Test
    public void identity() {
        User u = new User("aaa", "bbb");
        when(uc.identity("aaa")).thenReturn(ResponseEntity.status(CONFLICT).build());
        ResponseEntity response = uc.identity("aaa");
        assertEquals(response.getStatusCode(), CONFLICT);
    }
}