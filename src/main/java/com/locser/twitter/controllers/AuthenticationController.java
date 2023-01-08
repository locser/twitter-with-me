package com.locser.twitter.controllers;

import com.locser.twitter.exception.EmailAlreadyTakenException;
import com.locser.twitter.exception.EmailFailToSendException;
import com.locser.twitter.exception.UserDoesNotExistException;
import com.locser.twitter.models.ApplicationUser;
import com.locser.twitter.models.RegisterObject;
import com.locser.twitter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

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

    @ExceptionHandler({
            EmailFailToSendException.class
    })
    public ResponseEntity<String> handleEmailFailToSend() {
        return new ResponseEntity<String>("The email failed to send, try again in a moment",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            UserDoesNotExistException.class
    })
    public ResponseEntity<String> handleUserDoesnotExist() {
        return new ResponseEntity<String>("The user you looking for does not exist!",HttpStatus.NOT_FOUND);
    }

    //go to localhost/auth/register
    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegisterObject registerObject) {
        return userService.registerUser(registerObject);
    }

    @PutMapping("/update/phone")
    public  ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String, String> body) {
        String username= body.get("username");
        String phone = body.get("phone");

        ApplicationUser user = userService.getUserByUserName(username);

        user.setPhone(phone);

        return userService.updateUser(user);
    }
    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> body) {
        userService.generateEmailVerification(body.get("username"));

        return new ResponseEntity<String>("Verification code generated, email sent", HttpStatus.OK);

    }

}
