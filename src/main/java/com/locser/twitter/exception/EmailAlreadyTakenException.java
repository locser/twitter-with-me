package com.locser.twitter.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException() {
        super("The email provided is already taken");
    }
}
