package com.locser.twitter.controllers;

import com.locser.twitter.exception.EmailAlreadyTakenException;
import com.locser.twitter.models.ApplicationUser;
import com.locser.twitter.models.RegisterObject;
import com.locser.twitter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }


    @ExceptionHandler({
            EmailAlreadyTakenException.class
    })
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<String>("The email you provided is already taken",HttpStatus.CONFLICT);
    }

    //go to localhost/auth/register
    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegisterObject registerObject) {
        return userService.registerUser(registerObject);
    }
}
