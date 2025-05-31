package com.example.controller;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.images.Loading;

public class SceneManager {
    public static Scene switchScene(String type, Stage stage) throws IOException {
        if (type == "favorite") {
            FXMLLoader favoriteLoader = new FXMLLoader(SceneManager.class.getResource("/resources/fxml/Favorite.fxml"));
            Parent root = favoriteLoader.load();
            Favorite favoriteController = favoriteLoader.getController();
            int movieCount = favoriteController.loadFavoriteMovies();
            if (movieCount == 0) {
                JOptionPane.showMessageDialog(null, "你目前沒有收藏任何電影！", "Message", JOptionPane.INFORMATION_MESSAGE);
            }

            Scene favoriteScene = new Scene(root);
            String favoriteCSS = SceneManager.class.getResource("/resources/css/Favorite.css").toExternalForm();
            favoriteScene.getStylesheets().add(favoriteCSS);

            return favoriteScene;
        }
        else {
            Scene favoriteScene = new Scene(
                    new FXMLLoader(SceneManager.class.getResource("/resources/fxml/Favorite.fxml")).load());
            return favoriteScene;
        }
    }
}
