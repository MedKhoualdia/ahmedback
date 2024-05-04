package com.dance.mo.auth.Controller;

import com.dance.mo.Config.JwtService;
import com.dance.mo.Entities.User;
import com.dance.mo.Exceptions.UserException;
import com.dance.mo.Repositories.UserRepository;
import com.dance.mo.Services.UserService;
import com.dance.mo.auth.DTO.*;
import com.dance.mo.auth.Service.AuthenticationService;
import com.dance.mo.auth.EmailPwd.EmailPwd;
import com.dance.mo.auth.Service.EmailRegistrationService;
import com.dance.mo.auth.Service.FaceRecognitionService;
import com.dance.mo.auth.Service.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {
    @Value("${jwt.secret}")
    private  String SECRET_KEY;
    private static final long DEFAULT_EXPIRATION_TIME_MILLIS = 604800000;
    private final AuthenticationService service;
    private final EmailRegistrationService emailservice;
    private final UserService userService;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final FaceRecognitionService faceRecognitionService;
    public static Set<String> onlineUsers = new HashSet<>();
    private static final String CONFIRMATION_URL = "http://localhost:4200/forgot-password/%s";
    ///  endpoint : authenticate an existing user

    private final EmailPwd emailPwd;
    private String activationUrl="http://localhost:4200/activate-password";
    private final UserRepository userRepository;


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        onlineUsers.remove(email);
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //////////
    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<ForgotPasswordResponse> forgetPassword(@PathVariable String email) {
        User user = service.resetUserByEmail(email);
        ForgotPasswordResponse response = new ForgotPasswordResponse(user.getEmail());
         long resetToken = Long.parseLong(generateActivationCode(6));
        System.out.println(resetToken);
        user.setResetToken(resetToken);
        String jwtToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_TIME_MILLIS))
                .claim("resetToken", resetToken)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        userService.updateUser(user.getUserId(),user);
        try {
            emailPwd.sendEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    "resetpwd",
                    String.format(CONFIRMATION_URL, jwtToken),
                    String.valueOf(resetToken)
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());//0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
//////////

    @PostMapping("/forgot-password/")
    public ResponseEntity<ForgotPasswordResponse> CforgetPassword(@RequestBody CforgotPasswordRequest CRequest) {
        User user = service.resetUserByEmail(CRequest.getEmail());
        long cReset  = Long.parseLong(CRequest.getResetToken());
        if (Objects.equals(user.getResetToken() ,cReset)&&user.getEmail().equals(CRequest.getEmail())){
            String newPassword = CRequest.getNewPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            service.updateUser(user);
            return  ResponseEntity.ok(new ForgotPasswordResponse(CRequest.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ForgotPasswordResponse.builder()
                        .messageResponse("An error occurred during password reset")
                        .build());
    }


    @GetMapping("/onlineUsers")
    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }


    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            onlineUsers.add(response.getEmail());
            return ResponseEntity.ok(response);
        }
        catch (UserException e) {
            if (e.getMessage().equals("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        AuthenticationResponse.builder()
                                .messageResponse("User not found")
                                .build());
            } else if (e.getMessage().equals("User account is not active. Please confirm your email.")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        AuthenticationResponse.builder()
                                .messageResponse("User account is not active. Please confirm your email.")
                                .build());
            } else {
                // Handle any other UserException
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        AuthenticationResponse.builder()
                                .messageResponse("An error occurred during authentication")
                                .build());
            }
        }
    }
////////////////////////:face
    @PostMapping("/PasswordByFace")
    public ResponseEntity<FaceResponse> authenticateByFace(@RequestBody FaceRequest faceRequest) {
        try {
            User faceUser = userRepository.findByEmail(faceRequest.getEmail())
                    .orElseThrow(() -> new UserException("User not found"));

            boolean isAuthenticated = faceRecognitionService.compareImages(faceUser.getProfileImage(), faceRequest.getCapturedFaceImage());

            if (isAuthenticated) {
                String temporaryPassword = generateTemporaryPassword();
                faceUser.setPassword(passwordEncoder.encode(temporaryPassword));

                return ResponseEntity.ok(
                       FaceResponse.builder()
                                .message(temporaryPassword)
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        FaceResponse.builder()
                                .message("Face not recognized")
                                .build());
            }
        }catch (UserException e) {
            // Handle any exceptions
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    FaceResponse.builder()
                            .message("User not found")
                            .build());
        }

    }

    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
/////////////////////////////////////////:face

}



