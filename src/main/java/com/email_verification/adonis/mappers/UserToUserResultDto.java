package com.email_verification.adonis.mappers;

import com.email_verification.adonis.user.User;
import com.email_verification.adonis.user.UserResultDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserToUserResultDto implements Function<User, UserResultDto> {
    @Override
    public UserResultDto apply(User user) {

        UserResultDto userResultDto = new UserResultDto();
        userResultDto.setFirstName(user.getFirstName());
        userResultDto.setLastName(user.getLastName());
        userResultDto.setEmail(user.getEmail());
        userResultDto.setRole(user.getRole().toString());
        userResultDto.setEnabled(user.isEnabled());
        return userResultDto;
    }
}
