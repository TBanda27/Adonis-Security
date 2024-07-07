package com.email_verification.adonis.registration.token;


import com.email_verification.adonis.exception.BusinessValidationException;
import com.email_verification.adonis.user.User;
import com.email_verification.adonis.user.UserRepository;
import com.email_verification.adonis.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class VerificationTokenService implements  VerificationTokenServiceInterface{

    public final UserRepository userRepository;
    public final VerificationTokenRepository verificationTokenRepository;

    @Override
    public boolean verifyEmail(String token) {
        VerificationToken verificationToken = findByToken(token);
        if(verificationToken == null)
            throw new BusinessValidationException("Invalid Verification token");
        if(!validateToken(verificationToken))
            throw new BusinessValidationException("Invalid Verification token");

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public boolean validateToken(VerificationToken token) {
        if(token.getUser().isEnabled())
            throw new BusinessValidationException("User already validated.");
        if(token.getTokenExpirationTime().before(new Date()))
            throw new BusinessValidationException("Validation token already expired");
        return true;
    }

    @Scheduled(fixedDelay = 6000)
    public void deleteExpiredTokens(){
        List<VerificationToken> allTokens = verificationTokenRepository.findAll();

        for(VerificationToken verificationToken: allTokens){
            if(verificationToken.getTokenExpirationTime().before(new Date())){
                verificationTokenRepository.delete(verificationToken);
            }
        }
    }

}
