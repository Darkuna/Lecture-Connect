package com.lecture.coordinator.exceptions.user;

public class UserInvalidEmailException extends Exception{
    public UserInvalidEmailException(String message) {
        super(message);
    }
}
