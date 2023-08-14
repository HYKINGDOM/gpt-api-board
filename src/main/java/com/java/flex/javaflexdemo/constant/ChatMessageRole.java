package com.java.flex.javaflexdemo.constant;

/**
 * @author kscs
 */

public enum ChatMessageRole {

    USER("user"),

    system("system");


    private String value;

    ChatMessageRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
