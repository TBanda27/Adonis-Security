package com.email_verification.adonis.registration.token;

public interface VerificationTokenServiceInterface {

    VerificationToken findByToken(String token);

    boolean validateToken(VerificationToken token);

    boolean verifyEmail(String token);
}
