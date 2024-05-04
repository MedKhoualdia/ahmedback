package com.dance.mo.auth.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Oauth2Controller {
    @GetMapping("login/oauth2/code/google")
    public String handleGoogleCallback(Principal principal) {
        return principal.getName();
    }
}
