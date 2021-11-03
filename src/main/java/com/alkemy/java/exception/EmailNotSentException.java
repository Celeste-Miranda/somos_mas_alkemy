package com.alkemy.java.exception;

public class EmailNotSentException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EmailNotSentException(String msg) {super(msg);}
}
