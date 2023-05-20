package com.LearningAutopilot.Exceptions;

public class SQLExceptionMessageWrapper {

    public static String getWrappedSQLStateMessage(String SQLState, String Message) {
        if (SQLState == null)
            return Message;

        switch (SQLState) {
            case ("28000") -> {
                return "Неверный логин или пароль";
            }
            case ("42501") -> {
                return "Недостаточно прав для совершения данной операции";
            }
            default -> {
                return "SQLState: " + SQLState + "\n" + Message;
            }
        }
    }
}
