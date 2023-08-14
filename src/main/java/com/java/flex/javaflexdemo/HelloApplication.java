package com.java.flex.javaflexdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.java.flex.javaflexdemo.constant.CommonConstant.HEIGHT;
import static com.java.flex.javaflexdemo.constant.CommonConstant.TILE_SIZE;
import static com.java.flex.javaflexdemo.constant.CommonConstant.WIDTH;

/**
 * @author kscs
 */
public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        stage.setTitle("GPT-Go");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}