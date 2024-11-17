package com.example.demo.exceptions.scheduler;

public class NoCandidatesForCourseSessionException extends RuntimeException {
    public NoCandidatesForCourseSessionException(String message) {
        super(message);
    }
}
