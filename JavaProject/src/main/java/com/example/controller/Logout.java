package com.example.controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import resources.images.Loading;

import javax.swing.JOptionPane;

public class Logout {
    @FXML
    private Button favoriteButton;
    @FXML
    private Button deleteAccountButton;
    @FXML
    private Button turnbackButton;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    public void favoriteButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try {
                    FXMLLoader favoriteLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Favorite.fxml"));
                    Parent root = favoriteLoader.load();
                    Favorite favoriteController = favoriteLoader.getController();
                    favoriteController.loadFavoriteMovies();

                    Scene favoriteScene = new Scene(root);
                    String favoriteCSS = this.getClass().getResource("/resources/css/Favorite.css").toExternalForm();
                    favoriteScene.getStylesheets().add(favoriteCSS);

                    Platform.runLater(() -> {
                        stage.setScene(favoriteScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void deleteAccountButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try {
                    FXMLLoader deleteAccountLoader = new FXMLLoader(
                            getClass().getResource("/resources/fxml/DeleteAccount.fxml"));
                    Parent root = deleteAccountLoader.load();
                    DeleteAccount deleteAccountController = deleteAccountLoader.getController();
                    // deleteAccountController.setUserName(userName);

                    Scene deleteAccountScene = new Scene(root);
                    String deleteAccountCSS = this.getClass().getResource("/resources/css/DeleteAccount.css")
                            .toExternalForm();
                    deleteAccountScene.getStylesheets().add(deleteAccountCSS);

                    Platform.runLater(() -> {
                        stage.setScene(deleteAccountScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void turnbackButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try {
                    FXMLLoader recommendLoader = new FXMLLoader(
                            getClass().getResource("/resources/fxml/Recommend.fxml"));
                    Parent root = recommendLoader.load();
                    Recommend recommendController = recommendLoader.getController();
                    // recommendController.setUserName(userName);

                    Scene recommendScene = new Scene(root);
                    String recommendCSS = this.getClass().getResource("/resources/css/Recommend.css").toExternalForm();
                    recommendScene.getStylesheets().add(recommendCSS);

                    Platform.runLater(() -> {
                        stage.setScene(recommendScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void yesButtonOnAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("通知");
        alert.setHeaderText(null);
        alert.setContentText("已登出，即將跳轉到登入頁面");
        alert.showAndWait();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try {
                    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Login.fxml"));
                    Parent root = loginLoader.load();
                    Login loginController = loginLoader.getController();
                    // loginController.setUserName(userName);

                    Scene loginScene = new Scene(root);
                    String loginCSS = this.getClass().getResource("/resources/css/Login.css").toExternalForm();
                    loginScene.getStylesheets().add(loginCSS);

                    Platform.runLater(() -> {
                        stage.setScene(loginScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

}
