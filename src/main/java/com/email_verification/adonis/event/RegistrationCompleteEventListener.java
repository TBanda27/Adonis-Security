package com.email_verification.adonis.event;

import com.email_verification.adonis.user.User;
import com.email_verification.adonis.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@AllArgsConstructor
@Service
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    public final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

//        1.     Get the newly registered user
        User user = event.getUser();

//        2.     Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();

//        3.     Save the verification token for the user
        userService.saveUserVerificationToken(user, verificationToken);
//        4.     Build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "/api/v1/registration/verify_email?token=" + verificationToken;
        log.info("Click the link to complete registration {}", url);
//        5.     Send the email
    }
}
