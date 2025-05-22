package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Login.fxml"));
            Scene loginScene = new Scene(loginLoader.load());
            String loginCSS = this.getClass().getResource("/resources/css/Login.css").toExternalForm();
            loginScene.getStylesheets().add(loginCSS);
            
            ////////////////////////////////////////////////////////////
            
            Image icon = new Image("/resources/images/stackOverflowIcon.png");

            primaryStage.setTitle("自動化電影推薦");
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(loginScene);
            primaryStage.setWidth(1280);
            primaryStage.setHeight(800);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}