package com.dance.mo.auth.DTO;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordResponse {

    private String email;
    private String messageResponse;


    public ForgotPasswordResponse(String email) {
        this.email = email;
    }
}
