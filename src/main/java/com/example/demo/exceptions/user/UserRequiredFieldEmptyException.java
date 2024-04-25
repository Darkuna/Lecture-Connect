package com.example.demo.exceptions.user;

public class UserRequiredFieldEmptyException extends Exception {
    public UserRequiredFieldEmptyException(String field) {
        super(field);
    }
}
