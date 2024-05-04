package com.dance.mo.auth.Controller;

import com.dance.mo.Entities.User;
import com.dance.mo.auth.DTO.ProfileDto;
import com.dance.mo.auth.Service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final AuthenticationService authenticationService;

    @PostMapping("/upload-image")
    public ResponseEntity<User> uploadImage(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {
        User profileDto = authenticationService.getUser(authentication);
        authenticationService.uploadImage(profileDto, file);
        return ResponseEntity.ok(profileDto);
    }
    @GetMapping("/getProfile")
    public ResponseEntity<ProfileDto> getUserProfile(Authentication authentication) {
        System.out.println(authentication);
        ProfileDto profileDto = authenticationService.getProfile(authentication);
        return ResponseEntity.ok(profileDto);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<ProfileDto> updateUserProfile(Authentication authentication,@RequestBody ProfileDto profileDto) {
        System.out.println(profileDto.getId());
        ProfileDto updatedProfile = authenticationService.updateProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedProfile);
    }
}

