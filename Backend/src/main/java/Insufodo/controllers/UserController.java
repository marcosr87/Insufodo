package Insufodo.controllers;

import Insufodo.models.User;

import Insufodo.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService us;

    @PostMapping("")
    public ResponseEntity addUser(@RequestBody final @NotNull User u) {
        return us.addUser(u);
    }

    @PostMapping("/login")
    public String login(@RequestBody final @NotNull User u) {
        return us.login(u);
    }

    @GetMapping("/identity")
    @ApiResponses({
            @ApiResponse(code = 204, message = "usuario no existente"),
            @ApiResponse(code = 409, message = "usuario ya existente")
    })
    public ResponseEntity identity(@RequestParam final @NotNull String email) {
        return us.identity(email);
    }

    @GetMapping("/userManual")
    public ResponseEntity userManual(String fileName, HttpServletResponse res) throws Exception {
        return us.userManual(fileName, res);
    }
}