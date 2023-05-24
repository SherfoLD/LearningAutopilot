package com.LearningAutopilot.Exceptions;

public class SQLExceptionMessageWrapper {

    public static String getWrappedSQLStateMessage(String SQLState, String Message) {
        if (SQLState == null)
            return Message;

        switch (SQLState) {
            case ("22007"), ("22008") -> {
                return "Дата должна быть вида yyyy-mm-dd";
            }
            case ("23502") -> {
                return "Заполните все нужные поля";
            }
            case ("23503") -> {
                return "Существуют зависимости от этой записи";
            }
            case ("23505") -> {
                return "Одно из введённых полей не уникально";
            }
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
