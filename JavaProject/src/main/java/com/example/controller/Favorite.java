package com.example.controller;

import java.io.IOException;
import java.lang.Math;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.SVGPath;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import resources.images.Loading;
import com.example.service.Service;
import com.example.service.Movie;

public class Favorite implements Initializable {
    private String userName;

    @FXML
    private Button turnbackButton, deleteAccountButton, logoutButton;
    @FXML
    private Button movieButton1, movieButton2, movieButton3, movieButton4, movieButton5, movieButton6;
    @FXML
    private Label movieName1, movieTag1_1, movieTag1_2, movieTag1_3;
    @FXML
    private Label movieName2, movieTag2_1, movieTag2_2, movieTag2_3;
    @FXML
    private Label movieName3, movieTag3_1, movieTag3_2, movieTag3_3;
    @FXML
    private Label movieName4, movieTag4_1, movieTag4_2, movieTag4_3;
    @FXML
    private Label movieName5, movieTag5_1, movieTag5_2, movieTag5_3;
    @FXML
    private Label movieName6, movieTag6_1, movieTag6_2, movieTag6_3;
    @FXML
    private ImageView movieImage1, movieImage2, movieImage3, movieImage4, movieImage5, movieImage6;
    @FXML
    private Button starButton1, starButton2, starButton3, starButton4, starButton5, starButton6;
    @FXML
    private SVGPath starSVGPath1, starSVGPath2, starSVGPath3, starSVGPath4, starSVGPath5, starSVGPath6;

    private String fullStar = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
    private String emptyStar = "M12 17.27 18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z";
    private String movieID1, movieID2, movieID3, movieID4, movieID5, movieID6;
    private String[] movieIDs = {movieID1, movieID2, movieID3, movieID4, movieID5, movieID6};
    private String movieID = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void loadFavoriteMovies() throws IOException{
        Label[] movieNames = {movieName1, movieName2, movieName3, movieName4, movieName5, movieName6};
        Label[][] movieTags = {{movieTag1_1, movieTag1_2, movieTag1_3},
                                {movieTag2_1, movieTag2_2, movieTag2_3},
                                {movieTag3_1, movieTag3_2, movieTag3_3},
                                {movieTag4_1, movieTag4_2, movieTag4_3},
                                {movieTag5_1, movieTag5_2, movieTag5_3},
                                {movieTag6_1, movieTag6_2, movieTag6_3}};
        ImageView[] movieImages = {movieImage1, movieImage2, movieImage3, movieImage4, movieImage5, movieImage6};
        Button[] starButtons = {starButton1, starButton2, starButton3, starButton4, starButton5, starButton6};
        SVGPath[] starSVGPaths = {starSVGPath1, starSVGPath2, starSVGPath3, starSVGPath4, starSVGPath5, starSVGPath6};
        
        ArrayList<Movie> favoriteMovies = Service.listFavorite();
        for (int i = 0; i < Math.min(favoriteMovies.size(), 6); i++) {
            movieNames[i].setText(favoriteMovies.get(i).getTitle());

            for(int j = 0 ; j < Math.min(favoriteMovies.get(i).getGenres().size(), 3) ; j++){
                movieTags[i][j].setText(favoriteMovies.get(i).getGenres().get(j));
            }
            for(int j = favoriteMovies.get(i).getGenres().size() ; j < 3 ; j++){
                movieTags[i][j].setText("");
            }

            Image img = new Image(favoriteMovies.get(i).getPosterPath());
            movieImages[i].setImage(img);

            movieIDs[i] = favoriteMovies.get(i).getID();
        }
        for (int i = favoriteMovies.size(); i < 6; i++) {
            movieNames[i].setText("");

            for(int j = 0 ; j < 3 ; j++){
                movieTags[i][j].setText("");
            }

            Image img = new Image("file:/resources/images/empty.jpg");
            movieImages[i].setImage(img);

            starButtons[i].setStyle("-fx-background-color: #212121;");
            starSVGPaths[i].setContent("");
        }
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

    public void refreshButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try{
                    FXMLLoader favoriteLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Favorite.fxml"));
                    Parent root = favoriteLoader.load();
                    Favorite favoriteController = favoriteLoader.getController();
                    favoriteController.setUserName(userName);
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

    public void movieButton1OnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        if(event.getSource() == movieButton1) movieID = movieIDs[0];
        else if(event.getSource() == movieButton2) movieID = movieIDs[1];
        else if(event.getSource() == movieButton3) movieID = movieIDs[2];
        else if(event.getSource() == movieButton4) movieID = movieIDs[3];
        else if(event.getSource() == movieButton5) movieID = movieIDs[4];
        else if(event.getSource() == movieButton6) movieID = movieIDs[5];

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try{
                    FXMLLoader introductionLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Introduction.fxml"));
                    Parent root = introductionLoader.load();
                    Introduction introductionController = introductionLoader.getController();
                    introductionController.setMovieID(movieID);
                    introductionController.loadMovie();;

                    Scene introductionScene = new Scene(root);
                    String introductionCSS = this.getClass().getResource("/resources/css/Introduction.css").toExternalForm();
                    introductionScene.getStylesheets().add(introductionCSS);

                    Platform.runLater(() -> {
                        stage.setScene(introductionScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void deleteAccountButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try{
                    FXMLLoader deleteAccountLoader = new FXMLLoader(getClass().getResource("/resources/fxml/DeleteAccount.fxml"));
                    Parent root = deleteAccountLoader.load();
                    DeleteAccount deleteAccountController = deleteAccountLoader.getController();
                    //deleteAccountController.setUserName(userName);

                    Scene deleteAccountScene = new Scene(root);
                    String deleteAccountCSS = this.getClass().getResource("/resources/css/DeleteAccount.css").toExternalForm();
                    deleteAccountScene.getStylesheets().add(deleteAccountCSS);

                    Platform.runLater(() -> {
                        stage.setScene(deleteAccountScene);
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

    public void starButtonOnAction(ActionEvent event) throws IOException{
        String starButtonId = ((Button)event.getSource()).getId();
        SVGPath svgPath;
        if(starButtonId.equals("starButton1")) svgPath = starSVGPath1;
        else if(starButtonId.equals("starButton2")) svgPath = starSVGPath2;
        else if(starButtonId.equals("starButton3")) svgPath = starSVGPath3;
        else if(starButtonId.equals("starButton4")) svgPath = starSVGPath4;
        else if(starButtonId.equals("starButton5")) svgPath = starSVGPath5;
        else svgPath = starSVGPath6;

        if(svgPath.getContent() == fullStar){
            svgPath.setContent(emptyStar);
        }
        else{
            svgPath.setContent(fullStar);
        }
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }
}
