package com.dance.mo.Config;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.UserRepository;
import com.dance.mo.auth.DTO.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.dance.mo.auth.Controller.AuthenticationController.onlineUsers;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    AuthenticationResponse authResponse = new AuthenticationResponse();

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        System.out.println(authentication.getPrincipal());
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String provider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        System.out.println("Provider --> "+provider);
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Optional<User> existingUser = userRepository.findByEmail(oauth2User.getAttribute("email"));
        if (existingUser.isPresent()) {
            token(response, existingUser);
            onlineUsers.add(existingUser.get().getEmail());
        }
        else{
            User user = new User();
            user.setEnabled(true);
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(generateSecureRandomPassword()));
            user.setEmail(Objects.requireNonNull(oauth2User.getAttribute("email")));
            if (provider.equals("facebook")) {
                System.out.println(Optional.ofNullable(oauth2User.getAttribute("name")));
                user.setFirstName(Objects.requireNonNull(oauth2User.getAttribute("first_name")));
                user.setLastName(Objects.requireNonNull(oauth2User.getAttribute("last_name")));
            } else if (provider.equals("google")) {
                user.setFirstName(Objects.requireNonNull(oauth2User.getAttribute("given_name")));
                user.setLastName(Objects.requireNonNull(oauth2User.getAttribute("family_name")));
            }
            userRepository.save(user);
            onlineUsers.add(user.getEmail());
            token(response, Optional.of(user));
        }
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("http://localhost:4200/DanceScape/profile");
        super.onAuthenticationSuccess(request, response, authentication);

    }

    private void token(HttpServletResponse response, Optional<User> existingUser) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("role",existingUser.get().getRole().name());
        var jwtToken = jwtService.genToken(existingUser.get(), map);
        authResponse.setToken(jwtToken);
        authResponse.setMessageResponse("Login successful!");
        authResponse.setRole(existingUser.get().getRole());
        authResponse.setEmail(existingUser.get().getEmail());
        String redirectUrl = "http://localhost:4200/login?token=" + jwtToken;
        response.sendRedirect(redirectUrl);
    }
    private String generateSecureRandomPassword() {

        SecureRandom random = new SecureRandom();
        int passwordLength = 12;
        String passwordChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder password = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            password.append(passwordChars.charAt(random.nextInt(passwordChars.length())));
        }

        return password.toString();
    }
}
