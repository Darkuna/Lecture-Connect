package com.lecture.coordinator.exceptions.user;

public class UserRequiredFieldEmptyException extends Exception {
    public UserRequiredFieldEmptyException(String field) {
        super(field);
    }
}
