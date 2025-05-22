package com.example.controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import resources.images.Loading;
import com.example.service.Service;
import com.example.service.Movie;

public class Introduction {
    @FXML
    private Button turnbackButton;
    @FXML
    private Button starButton;
    @FXML
    private SVGPath starSVGPath;
    @FXML
    private Label movieName, movieTag1, movieTag2, movieTag3, Scipt;
    @FXML
    private Label[] movieTags;
    @FXML
    private ImageView movieImage;
    @FXML
    private Hyperlink hyperlink;
    
    private String fullStar = "M12 17.27 18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z";
    private String emptyStar = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
    private String movieID = "";
    private Movie movie;

    public void loadMovie() throws IOException{
        try{
            movieTags = new Label[] {movieTag1, movieTag2, movieTag3};
            movie = Service.getMovieDetail(movieID);
            movieName.setText(movie.getTitle());
            for(int i = 0 ; i < Math.min(movie.getGenres().size(), 3) ; i++){
                movieTags[i].setText(movie.getGenres().get(i));
            }
            for(int i = movie.getGenres().size() ; i < 3 ; i++){
                movieTags[i].setText("");
            }
            movieImage.setImage(new Image(movie.getPosterPath()));
            Scipt.setText(movie.getOverview());
            if(Service.checkIfFavorite(movieID)){
                starSVGPath.setContent(fullStar);
            }
            else{
                starSVGPath.setContent(emptyStar);
            }
            hyperlink.setText(movie.getLink());
        }
        catch(NullPointerException e){
            System.err.println("Null Pointer Exception!!");
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

    public void starButtonOnAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Loading loading = new Loading(stage);
        loading.show();

        Boolean currentIsEmptyStar = starSVGPath.getContent().equals(emptyStar);

        new Thread(() -> {
            try{
                if(currentIsEmptyStar){
                    Service.addFavorite(movieID);
                }
                else{
                    Service.removeFavorite(movieID);
                }

                Platform.runLater(() -> {
                    starSVGPath.setContent(currentIsEmptyStar? fullStar : emptyStar);
                    loading.closeStage();
                });
            }
            catch(Exception e){
                e.printStackTrace();
                Platform.runLater(() -> {
                    loading.closeStage();
                });
            }
        }).start();
    }

    public void linkClickOnAction(ActionEvent event){
        try{
            java.awt.Desktop.getDesktop().browse(new java.net.URI(hyperlink.getText()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setMovieID(String movieID){
        this.movieID = movieID;
    }
}
