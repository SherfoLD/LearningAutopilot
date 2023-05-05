package com.LearningAutopilot.Exceptions;

public class InvalidConfigException extends Exception {

    public InvalidConfigException(String s) {
        super(s);
    }

    public InvalidConfigException(String s, Exception e) {
        super(s, e);
    }
}
