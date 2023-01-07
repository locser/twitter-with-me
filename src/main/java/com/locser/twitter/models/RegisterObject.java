package com.locser.twitter.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
public class RegisterObject {
    private  String firstName;
    private String lastName;
    private String email;
    private Date dob;

    public RegisterObject() {
    }

    public RegisterObject(String firstName, String lastName, String email, Date dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
    }
}

