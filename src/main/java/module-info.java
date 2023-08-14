module com.java.flex.javaflexdemo {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires com.google.common;
    requires cn.hutool.core;
    requires com.google.gson;
    requires guava.retrying;

    opens com.java.flex.javaflexdemo.domain to com.google.gson;
    opens com.java.flex.javaflexdemo to javafx.fxml;
    exports com.java.flex.javaflexdemo;
    exports com.java.flex.javaflexdemo.util;
    opens com.java.flex.javaflexdemo.util to javafx.fxml;
}