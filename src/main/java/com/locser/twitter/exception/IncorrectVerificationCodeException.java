package com.locser.twitter.exception;

public class IncorrectVerificationCodeException extends RuntimeException {

    public IncorrectVerificationCodeException() {
        super("The code passed did not match the users verification code!");
    }
}
