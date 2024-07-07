package com.email_verification.adonis.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class UserResultDto {

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    private boolean isEnabled;
}
