package com.example.demo.exceptions.user;

public class UserInvalidEmailException extends Exception{
    public UserInvalidEmailException(String message) {
        super(message);
    }
}
