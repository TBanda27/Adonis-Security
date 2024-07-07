package com.email_verification.adonis.registration;


import com.email_verification.adonis.event.RegistrationCompleteEvent;
import com.email_verification.adonis.mappers.UserResultDtoToUser;
import com.email_verification.adonis.registration.token.VerificationTokenService;
import com.email_verification.adonis.user.PasswordResetRequest;
import com.email_verification.adonis.user.User;
import com.email_verification.adonis.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
@AllArgsConstructor
@Slf4j
public class UserRegistrationController {
    public final UserService userService;

    public final UserResultDtoToUser toUser;
    public final VerificationTokenService tokenService;

    public final ApplicationEventPublisher publisher;

    @PostMapping()
    ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest,
                                        final HttpServletRequest request){

        log.info("Request to register new user with : "+ userRegistrationRequest.toString());
        User user = userService.registerUser(userRegistrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
         return ResponseEntity.ok("Please check your email to verify your email and complete registration");
    }

    @GetMapping("/verify_email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token){

        log.info("Request to verify email with token: {}", token);

        if(tokenService.verifyEmail(token))
            return ResponseEntity.ok("Email successfully verified,  you can now log into your account.");
        else
            return ResponseEntity.ok("Email could not be verified.");
    }

    @PutMapping("/reset_password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email,
                                                 @RequestBody PasswordResetRequest passwordResetRequest
                                                 ){
        log.info("Request to change password for user with email: {}", email);
        return ResponseEntity.ok(userService.resetPassword(email, passwordResetRequest));
    }

    public String applicationUrl(HttpServletRequest request) {

        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
