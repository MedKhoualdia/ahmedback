package com.dance.mo.auth.DTO;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProfileDto {
    private Long id ;
    private String firstName ;
    private String lastName ;
    private Date birthday;
    private String email;
    private Role role;
    private Integer phoneNumber;
    private byte[] profileImage;
    // other fields

    public static ProfileDto fromEntity(User user)
    {
        if(user == null)
        {
            return null ;
        }

        return ProfileDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .profileImage(user.getProfileImage())
                .build() ;
    }


}
