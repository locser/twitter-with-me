package com.locser.twitter.exception;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super("This user you looking for does not exist");
    }
}
