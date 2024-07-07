package com.email_verification.adonis.controller;


import com.email_verification.adonis.user.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping()
    ResponseEntity<List<UserResultDto>> getAllUsers(){

        log.info("Request to get all users in the system");
        return ResponseEntity.ok(userService.findAllUsers());
    }

}
