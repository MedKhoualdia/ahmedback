package com.dance.mo.auth.Service;

import com.dance.mo.Config.JwtService;
import com.dance.mo.Config.PasswordEncoder;
import com.dance.mo.Entities.User;
import com.dance.mo.Exceptions.UserException;
import com.dance.mo.Repositories.UserRepository;
import com.dance.mo.auth.DTO.AuthenticationRequest;
import com.dance.mo.auth.DTO.AuthenticationResponse;
import com.dance.mo.auth.DTO.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;



    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()

                )
        );
        System.out.println("before user");

        var user = userRepository.getUserByEmail(request.getEmail());
        System.out.println("user");
        if (user == null) {
            throw new UserException("User not found");
        }
       else {

            if (!user.isEnabled()) {
                System.out.println("user disable");
                throw new UserException("User account is not active. Please confirm your email.");
            } else {
                System.out.println("before generate");
                Map<String, String> map = new HashMap<>();
                map.put("role",user.getRole().name());
               var jwtToken = jwtService.genToken(user, map);
                System.out.println("after generate");
               return AuthenticationResponse.builder()
                       .token(jwtToken)
                       .role(user.getRole())
                       .email(user.getEmail())
                       .build();
           }
       }
    }

    public ProfileDto getProfile(Authentication authentication)
    {
        var user = userRepository.getUserByEmail(authentication.getName());
        return ProfileDto.fromEntity(user);

    }
    public User getUser(Authentication authentication)
    {
        return userRepository.getUserByEmail(authentication.getName());
    }
    public User resetUserByEmail(String email)
    {

        return userRepository.getUserByEmail(email);
    }
    public void uploadImage(User userProfile, MultipartFile file) throws IOException {
        userProfile.setProfileImage(file.getBytes());
        userRepository.save(userProfile);
    }
    public void updateUser(User userProfile)  {
        userRepository.save(userProfile);
    }
    public ProfileDto updateProfile(ProfileDto profileDto)
    {
        var user = userRepository.findById(profileDto.getId()).orElseThrow(() -> new UsernameNotFoundException("user doesnt exits"));
        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setEmail(profileDto.getEmail());
        user.setBirthday(profileDto.getBirthday());
        user.setPhoneNumber(profileDto.getPhoneNumber());
        user.setRole(profileDto.getRole());


        // other fields
        var userSaved = userRepository.save(user);
        return    ProfileDto.fromEntity(userSaved)  ;

    }
}


