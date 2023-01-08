package com.locser.twitter.exception;

public class EmailFailToSendException extends RuntimeException {

    public EmailFailToSendException() {
        super("The email failed to send!");
    }
}
