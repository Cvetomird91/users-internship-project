package com.scalefocus.libraryproject.exceptions;

public class EmailNotExistingException extends RuntimeException{

    public EmailNotExistingException(String emailDoesNotExist) {
        super(emailDoesNotExist);
    }
}
