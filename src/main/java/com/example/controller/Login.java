package com.example.controller;

import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import resources.images.Loading;
import com.example.service.Movie;
import com.example.service.PopupMessageBuilder;
import com.example.service.Service;

public class Login {
    private String userName;

    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    // private String salt = "javaproject505";

    public void loginButtonOnAction(ActionEvent event) throws IOException, Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (usernameTextField.getText().isBlank() == true || passwordTextField.getText().isBlank() == true) {
            loginMessageLabel.setText("請輸入帳號和密碼以登入");
            return;
        }

        if (!vaildLogin(usernameTextField.getText(), passwordTextField.getText())) {
            loginMessageLabel.setText("登入失敗！帳號或密碼錯誤");
            return;
        }

        loginMessageLabel.setText("登入成功！請等待片刻...");
        userName = usernameTextField.getText();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try {
                    FXMLLoader recommendLoader = new FXMLLoader(
                            getClass().getResource("/resources/fxml/Recommend.fxml"));
                    Parent root = recommendLoader.load();
                    Recommend recommendController = recommendLoader.getController();
                    recommendController.setUserName(userName);
                    Service.getSession();
                    recommendController.loadMovie();

                    Scene recommendScene = new Scene(root);
                    String recommendCSS = this.getClass().getResource("/resources/css/Recommend.css").toExternalForm();
                    recommendScene.getStylesheets().add(recommendCSS);

                    ArrayList<String> genres = Service.getFavoriteGenres();
                    ArrayList<Movie> upcomingMovies = Service.getUpComing();
                    ArrayList<Movie> interestingUpcomingMovies = new ArrayList<Movie>();
                    for (int i = 0; i < upcomingMovies.size(); i++) {
                        for (int j = 0; j < upcomingMovies.get(i).getGenres().size(); j++) {
                            if (genres.contains(upcomingMovies.get(i).getGenres().get(j))) {
                                interestingUpcomingMovies.add(upcomingMovies.get(i));
                                System.out.println(upcomingMovies.get(i).getTitle());
                                break;
                            }
                        }
                    }

                    if (interestingUpcomingMovies.size() != 0) {
                        int index = (int) (Math.random() * interestingUpcomingMovies.size());
                        PopupMessageBuilder.showNotification(interestingUpcomingMovies.get(index).getTitle());
                    }

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

    public void signupButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (usernameTextField.getText().isBlank() == true || passwordTextField.getText().isBlank() == true) {
            loginMessageLabel.setText("請輸入帳號和密碼以註冊");
            return;
        }

        if (!vaildSignup(usernameTextField.getText(), passwordTextField.getText())) {
            loginMessageLabel.setText("註冊失敗！帳號已存在");
            return;
        }

        // 原本會進入表單頁面，現在改成跳出彈跳視窗
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("通知");
        alert.setHeaderText(null);
        alert.setContentText("其實我們沒有註冊的部分啦！\n請去TMDB註冊帳號後再使用此系統");
        alert.showAndWait();
        loginMessageLabel.setText("");

        // loginMessageLabel.setText("註冊成功！請等待片刻...");
        // Platform.runLater(() -> {
        // Loading loading = new Loading(stage);
        // loading.show();

        // new Thread(() -> {
        // try {
        // FXMLLoader formLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Form.fxml"));
        // Parent root = formLoader.load();
        // Form formController = formLoader.getController();

        // Scene formScene = new Scene(root);
        // String formCSS = this.getClass().getResource("/resources/css/Form.css").toExternalForm();
        // formScene.getStylesheets().add(formCSS);

        // Platform.runLater(() -> {
        // stage.setScene(formScene);
        // loading.closeStage();
        // });
        // }
        // catch (Exception e) {
        // e.printStackTrace();
        // }
        // }).start();
        // });

    }

    public boolean vaildLogin(String userName, String originPassword) throws IOException {
        // 如果有註冊的話會有的hash部分
        // String saltedPassword = salt + originPassword;
        // System.out.println("Origin password adding salt: " + saltedPassword);
        // System.out.println("Hashed password: " + hash(saltedPassword));
        Service.getToken();
        return Service.vaildLogin(userName, originPassword);
    }

    public boolean vaildSignup(String userName, String originPassword) {
        // 如果有註冊的話會有的hash部分2
        // String saltedPassword = salt + originPassword;
        // System.out.println("User name: " + userName);
        // System.out.println("Origin password adding salt: " + saltedPassword);
        // System.out.println("Hashed password: " + hash(saltedPassword));
        return true;
    }

    // public String hash(String originPassWord) {
    // try {
    // MessageDigest md = MessageDigest.getInstance("SHA-256");
    // byte[] messageDigest = md.digest(originPassWord.getBytes());

    // BigInteger no = new BigInteger(1, messageDigest);
    // String hashedPassword = no.toString(16);
    // while (hashedPassword.length() < 64) {
    // hashedPassword = "0" + hashedPassword;
    // }
    // return hashedPassword;
    // }
    // catch (NoSuchAlgorithmException e) {
    // throw new RuntimeException(e);
    // }
    // }
}
