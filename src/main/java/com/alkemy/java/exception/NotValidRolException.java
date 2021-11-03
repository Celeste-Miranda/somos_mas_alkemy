package com.alkemy.java.exception;

public class NotValidRolException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotValidRolException(String msg) {
        super(msg);
    }
}
