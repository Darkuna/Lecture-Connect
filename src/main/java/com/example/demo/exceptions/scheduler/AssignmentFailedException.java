package com.example.demo.exceptions.scheduler;

public class AssignmentFailedException extends RuntimeException {
    public AssignmentFailedException(String message) {
        super(message);
    }
}
