package com.example.controller;

import java.io.IOException;
import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import resources.images.Loading;

public class DeleteAccount {
    @FXML
    private Button favoriteButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button turnbackButton;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    public void favoriteButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try{
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
                catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void logoutButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try{
                    FXMLLoader logoutLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Logout.fxml"));
                    Parent root = logoutLoader.load();
                    Logout logoutController = logoutLoader.getController();
                    //logoutController.setUserName(userName);

                    Scene logoutScene = new Scene(root);
                    String logoutCSS = this.getClass().getResource("/resources/css/Logout.css").toExternalForm();
                    logoutScene.getStylesheets().add(logoutCSS);

                    Platform.runLater(() -> {
                        stage.setScene(logoutScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void turnbackButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try{
                    FXMLLoader recommendLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Recommend.fxml"));
                    Parent root = recommendLoader.load();
                    Recommend recommendController = recommendLoader.getController();
                    //recommendController.setUserName(userName);

                    Scene recommendScene = new Scene(root);
                    String recommendCSS = this.getClass().getResource("/resources/css/Recommend.css").toExternalForm();
                    recommendScene.getStylesheets().add(recommendCSS);

                    Platform.runLater(() -> {
                        stage.setScene(recommendScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void yesButtonOnAction(ActionEvent event) throws IOException{
        String[] options = {"是", "否"};
        var selection = JOptionPane.showOptionDialog(null, "確定要刪除帳戶嗎？", "Message", 0, 1, null, options, options[0]);

        if(selection == 0){
            JOptionPane.showMessageDialog(null, "已刪除帳戶，即將跳轉到登入頁面", "Message", JOptionPane.INFORMATION_MESSAGE);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            Platform.runLater(() -> {
                Loading loading = new Loading(stage);
                loading.show();

                new Thread(() -> {
                    try{
                        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Login.fxml"));
                        Parent root = loginLoader.load();
                        Login loginController = loginLoader.getController();
                        //loginController.setUserName(userName);

                        Scene loginScene = new Scene(root);
                        String loginCSS = this.getClass().getResource("/resources/css/Login.css").toExternalForm();
                        loginScene.getStylesheets().add(loginCSS);

                        Platform.runLater(() -> {
                            stage.setScene(loginScene);
                            loading.closeStage();
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
            });
        }
    }
}
