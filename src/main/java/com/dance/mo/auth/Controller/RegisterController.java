package com.dance.mo.auth.Controller;

import com.dance.mo.Exceptions.UserException;
import com.dance.mo.auth.DTO.RegisterRequest;
import com.dance.mo.auth.Service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class RegisterController {
    private final RegistrationService service;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            String jwtToken = service.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
        }catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering user.");
        }
    }
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam String token) {
        try {
            String confirmationMessage = service.confirm(token);
            return ResponseEntity.ok(confirmationMessage);
        }catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while confirming user.");
        }
    }


}
