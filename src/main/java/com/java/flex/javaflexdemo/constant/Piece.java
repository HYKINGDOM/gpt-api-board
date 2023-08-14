package com.java.flex.javaflexdemo.constant;

/**
 * @author kscs
 */

public enum Piece {

    NONE("无子", 0),

    WHITE("白子", 1), BLACK("黑子", 2);

    private String name;

    private int value;

    Piece(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
