package com.example.demo.exceptions.roomTable;

public class NotEnoughSpaceAvailableException extends RuntimeException {
    public NotEnoughSpaceAvailableException(String text) {super(text);}
}
