package com.example.postcommentauthapi.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;

    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z_0-9`~!@#$%^&*()+|={};:,.<>/?]*$")
    private String password;

    private boolean admin = false;
    private String adminToken ="";
}
