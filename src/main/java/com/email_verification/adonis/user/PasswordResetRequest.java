package com.email_verification.adonis.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {

    private String password;

    private String verifyPassword;
}
