package com.dance.mo.auth.Controller;

import com.dance.mo.auth.Service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Confirm {

    @Autowired
    private  RegistrationService service;


    @GetMapping("/succes/{token}")
    public String confirmUser(@PathVariable String token) {
        String confirmationMessage = service.confirm(token);

        return confirmationMessage;
    }
    }
