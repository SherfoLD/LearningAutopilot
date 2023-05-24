package com.LearningAutopilot.Exceptions;

public class InvalidFieldDataException extends Exception {

    public InvalidFieldDataException(String invalidField) {
        super(invalidField);
    }
}
