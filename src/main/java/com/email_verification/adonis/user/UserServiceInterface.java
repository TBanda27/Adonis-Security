package com.email_verification.adonis.user;

import com.email_verification.adonis.registration.UserRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    List<UserResultDto> findAllUsers();

    User registerUser(UserRegistrationRequest userRegistrationRequest);

    UserResultDto getUserByEmail(String email);

    void saveUserVerificationToken(User user, String verificationToken);

    String login(UserLoginDetails userLoginDetails);
}
