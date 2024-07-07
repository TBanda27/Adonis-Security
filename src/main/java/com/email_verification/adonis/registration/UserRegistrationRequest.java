package com.email_verification.adonis.registration;

import com.email_verification.adonis.enums.Role;

public record UserRegistrationRequest(  String firstName,
                                        String lastName,
                                        String email,

                                        String password,
                                        Role role
                                        ) {

}
