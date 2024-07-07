package com.email_verification.adonis.user;

import com.email_verification.adonis.enums.Role;
import com.email_verification.adonis.exception.BusinessValidationException;
import com.email_verification.adonis.mappers.UserToUserResultDto;
import com.email_verification.adonis.registration.UserRegistrationRequest;
import com.email_verification.adonis.registration.token.VerificationToken;
import com.email_verification.adonis.registration.token.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService implements  UserServiceInterface{
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserToUserResultDto toUserResultDto;

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public List<UserResultDto> findAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(toUserResultDto)
                .collect(Collectors.toList());
    }

    @Override
    public User registerUser(UserRegistrationRequest userRegistrationRequest) {
        if(userRepository.findByEmail(userRegistrationRequest.email()).isPresent()){
            throw new BusinessValidationException("User with email: "+ userRegistrationRequest.email() + " already exists!");
        }
        User newUser = new User();
        newUser.setFirstName(userRegistrationRequest.firstName());
        newUser.setLastName(userRegistrationRequest.lastName());
        if(checkEmailValidity(userRegistrationRequest.email())){
            newUser.setEmail(userRegistrationRequest.email());
        }
        else throw new BusinessValidationException("Email format not valid");
        if(checkPasswordValidity(userRegistrationRequest.password())){
            newUser.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));
        }
        else throw new BusinessValidationException("Password should contain: \n" +
                "At least one uppercase letter \n"+
                "At least one number \n" +
                "At least one special case \n"+
                "At least 8 characters long"
        );
        if(userRegistrationRequest.role() == null){
            newUser.setRole(Role.USER);
        }
        else newUser.setRole(userRegistrationRequest.role());

        return userRepository.save(newUser);
    }

    @Override
    public UserResultDto getUserByEmail(String email) {
        User user = getUser(email);

        return toUserResultDto.apply(user);
    }

    @Override
    public void saveUserVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String login(UserLoginDetails userLoginDetails) {
        User user = getUser(userLoginDetails.getUsername());
        return user != null ? "You have successfully logged in "+ userLoginDetails.getUsername() : "Could not be logged in";
    }

    public User getUser(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new BusinessValidationException("User with email "+ email + " not found.")
        );
    }


    public boolean checkEmailValidity(String email){
        String emailRegex = "^[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
    }

    public boolean checkPasswordValidity(String password){
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&~`])[A-Za-z\\d@$!%*?&,.;:'\"{}|]{8,}$";

        Pattern pattern = Pattern.compile(passwordRegex);

        return pattern.matcher(password).matches();
    }

    public String resetPassword(String email, PasswordResetRequest passwordResetRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new BusinessValidationException("User with email " + email + " not found")
        );

        if(!passwordResetRequest.getPassword().equals(passwordResetRequest.getVerifyPassword()))
            throw new BusinessValidationException("Entered passwords do not match");

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        userRepository.save(user);

        return "Password for " + user.getEmail() + " successfully changed";
    }
}
