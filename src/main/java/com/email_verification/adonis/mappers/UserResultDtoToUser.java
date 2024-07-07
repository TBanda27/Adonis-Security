package com.email_verification.adonis.mappers;

import com.email_verification.adonis.enums.Role;
import com.email_verification.adonis.user.User;
import com.email_verification.adonis.user.UserResultDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserResultDtoToUser implements Function<UserResultDto, User> {

    @Override
    public User apply(UserResultDto userResultDto) {

        User user = new User();
        user.setFirstName(userResultDto.getFirstName());
        user.setLastName(userResultDto.getLastName());
        user.setEmail(userResultDto.getEmail());
        user.setRole(Role.valueOf(userResultDto.getRole()));

        return user;
    }
}
