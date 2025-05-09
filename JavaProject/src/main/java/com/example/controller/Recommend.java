package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import resources.images.Loading;

public class Recommend {
    private String userName;

    @FXML
    private CheckBox adventureCheckBox, dramaCheckBox, actionCheckBox, animationCheckBox, historyCheckBox,  comedyCheckBox;
    @FXML
    private CheckBox fantasyCheckBox, familyCheckBox, horrorCheckBox, mysteryCheckBox, thrillerCheckBox, warCheckBox;
    @FXML
    private CheckBox romanceCheckBox, crimeCheckBox, tvmovieCheckBox, scifiCheckBox, documentaryCheckBox;
    @FXML
    private CheckBox westernCheckBox, musicCheckBox;

    @FXML
    private Button submitButton,  accountButton;
    @FXML
    private Label recommendMessageLabel;

    @FXML
    private Button movieButton1,  movieButton2, movieButton3, movieButton4, movieButton5, movieButton6;
    @FXML
    private Label movieName1, movieName2, movieName3, movieName4, movieName5, movieName6;
    @FXML
    private Label movieTag1_1, movieTag1_2, movieTag1_3;
    @FXML
    private Label movieTag2_1, movieTag2_2, movieTag2_3;
    @FXML
    private Label movieTag3_1, movieTag3_2, movieTag3_3;
    @FXML
    private Label movieTag4_1, movieTag4_2, movieTag4_3;
    @FXML
    private Label movieTag5_1, movieTag5_2, movieTag5_3;
    @FXML
    private Label movieTag6_1, movieTag6_2, movieTag6_3;


    public void submitButtonOnAction(ActionEvent event){
        ArrayList<String> genres = new ArrayList<>();

        if(adventureCheckBox.isSelected())   genres.add("Adventure");
        if(dramaCheckBox.isSelected())       genres.add("Drama");
        if(actionCheckBox.isSelected())      genres.add("Action");
        if(animationCheckBox.isSelected())   genres.add("Animation");
        if(historyCheckBox.isSelected())     genres.add("History");
        if(comedyCheckBox.isSelected())      genres.add("Comedy");
        if(fantasyCheckBox.isSelected())     genres.add("Fantasy");
        if(familyCheckBox.isSelected())      genres.add("Family");
        if(horrorCheckBox.isSelected())      genres.add("Horror");
        if(mysteryCheckBox.isSelected())     genres.add("Mystery");
        if(thrillerCheckBox.isSelected())    genres.add("Thriller");
        if(warCheckBox.isSelected())         genres.add("War");
        if(romanceCheckBox.isSelected())     genres.add("Romance");
        if(crimeCheckBox.isSelected())       genres.add("Crime");
        if(tvmovieCheckBox.isSelected())     genres.add("Tvmovie");
        if(scifiCheckBox.isSelected())       genres.add("Scifi");
        if(documentaryCheckBox.isSelected()) genres.add("Documentary");
        if(westernCheckBox.isSelected())     genres.add("Western");
        if(musicCheckBox.isSelected())       genres.add("Music");

        if(genres.size() == 0){
            recommendMessageLabel.setText("請至少選擇1個類型");
            return;
        }
        
        recommendMessageLabel.setText("");
        for(int i = 0 ; i < genres.size() ; i++){
            System.out.println(genres.get(i));
        }
    }

    public void accountButtonOnAction(ActionEvent event) throws IOException{
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

    public void movieButtonOnAction(ActionEvent event) throws IOException{
        String movieButtonId = ((Button)event.getSource()).getId();
        Label movieLabel;
        String movieName;
        if(movieButtonId.equals("movieButton1")) movieLabel = movieName1;
        else if(movieButtonId.equals("movieButton2")) movieLabel = movieName2;
        else if(movieButtonId.equals("movieButton3")) movieLabel = movieName3;
        else if(movieButtonId.equals("movieNutton4")) movieLabel = movieName4;
        else if(movieButtonId.equals("movieNutton4")) movieLabel = movieName5;
        else movieLabel = movieName6;

        movieName = movieLabel.getText();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try{
                    FXMLLoader introductionLoader = new FXMLLoader(getClass().getResource("/resources/fxml/Introduction.fxml"));
                    Parent root = introductionLoader.load();
                    Introduction introductionController = introductionLoader.getController();

                    ///要改，ID沒放
                    introductionController.setMovieID("505");
                    introductionController.loadMovie();

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

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }
}
