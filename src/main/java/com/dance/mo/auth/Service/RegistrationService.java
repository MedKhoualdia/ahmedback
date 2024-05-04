package com.dance.mo.auth.Service;

import com.dance.mo.Config.JwtService;
import com.dance.mo.Config.PasswordEncoder;
import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import com.dance.mo.Exceptions.UserException;
import com.dance.mo.Repositories.UserRepository;
import com.dance.mo.auth.DTO.RegisterRequest;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final TemplateEngine templateEngine;

    private final UserRepository userRepository;
    private static final String CONFIRMATION_URL = "http://localhost:8088/succes/%s";
    private final PasswordEncoder passwordEncoder;
    private final EmailRegistrationService emailservice;
    private final JwtService jwtService;
    private final RedisService redisService;

    @Transactional
    public String  register(RegisterRequest request) {
        boolean userExists = userRepository.findByEmail(request.getEmail()).isPresent();
        if (userExists) {
            throw new UserException("A user already exists with the same email");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .birthday(request.getBirthday())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.bCryptPasswordEncoder().encode(request.getPassword()))
                .role(request.getRole())
                .enabled(false)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.genToken(user,new HashMap<>());

        redisService.storeToken(jwtToken, user.getEmail());
        try {
            emailservice.send(
                    user.getEmail(),
                    user.getFirstName(),
                    "confirm-email",
                    String.format(CONFIRMATION_URL, jwtToken)
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return jwtToken ;

    }

    public String confirm(String token) {

        String userEmail = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new UserException("User not found"+ userEmail));
        if (!user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
           //redisService.removeToken(user.getEmail());
            return "succes";
        }
        else if (!user.isEnabled() && jwtService.isTokenExpired(token)){

                // Handle token expiration
                return handleExpiredToken(userEmail,token);
            }
        else {
                // Token is valid but user is already confirmed
            return "already";
            }
        }

    private String renderTemplate(String templateName) {
        Context context = new Context();
        // Add any necessary model attributes to the context
        return templateEngine.process(templateName, context);
    }


    private String handleExpiredToken(String userEmail, String token) {
        String RefreshedToken = jwtService.refreshExpiredToken(token);
        redisService.removeToken(userEmail);

        redisService.storeToken(RefreshedToken, userEmail);
        var user = userRepository.getUserByEmail(userEmail);
        try {
            emailservice.send(
                    userEmail,
                    user.getFirstName(),
                    null,
                    String.format(CONFIRMATION_URL, RefreshedToken)
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return  "Token expired, a new token has been sent to your email";
    }

}
































