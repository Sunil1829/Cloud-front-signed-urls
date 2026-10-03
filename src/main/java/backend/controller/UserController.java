package backend.controller;

import backend.dto.LoginRequest;
import backend.dto.LoginResponse;
import backend.dto.UserRequest;
import backend.entity.User;
import backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @Valid @RequestBody UserRequest request) {

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                userService.createUser(user)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id) {

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String token = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }
}