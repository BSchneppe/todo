package com.simplesystem.todo.models;

public enum StatusEnum {
    NOT_DONE("not done"),
    DONE("done"),
    PAST_DUE("past due"),
    ;

    private final String description;

    StatusEnum(String description) {
        this.description = description;
    }
}
