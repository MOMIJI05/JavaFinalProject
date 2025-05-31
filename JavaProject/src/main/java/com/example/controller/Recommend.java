package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.JSONException;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import com.example.service.Service;
import com.example.service.Movie;
import javafx.stage.Stage;
import resources.images.Loading;

public class Recommend {
    private String userName;

    @FXML
    private CheckBox adventureCheckBox, dramaCheckBox, actionCheckBox, animationCheckBox, historyCheckBox,
            comedyCheckBox;
    @FXML
    private CheckBox fantasyCheckBox, familyCheckBox, horrorCheckBox, mysteryCheckBox, thrillerCheckBox, warCheckBox;
    @FXML
    private CheckBox romanceCheckBox, crimeCheckBox, tvmovieCheckBox, scifiCheckBox, documentaryCheckBox;
    @FXML
    private CheckBox westernCheckBox, musicCheckBox;

    @FXML
    private Button submitButton, accountButton, refreshButton;
    @FXML
    private Label recommendMessageLabel;

    @FXML
    private Button movieButton1, movieButton2, movieButton3, movieButton4, movieButton5, movieButton6;
    @FXML
    private Label movieName1, movieName2, movieName3, movieName4, movieName5, movieName6;
    @FXML
    private HBox movieTagBox1, movieTagBox2, movieTagBox3, movieTagBox4, movieTagBox5, movieTagBox6;
    @FXML
    private ImageView movieImage1, movieImage2, movieImage3, movieImage4, movieImage5, movieImage6;

    private String movieID1, movieID2, movieID3, movieID4, movieID5, movieID6;
    private String[] movieIDs = { movieID1, movieID2, movieID3, movieID4, movieID5, movieID6 };
    private String movieID = "";

    public void submitButtonOnAction(ActionEvent event) throws IOException, IndexOutOfBoundsException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ArrayList<String> genres = new ArrayList<String>();
        // ArrayList<Movie> recommendMovies = new ArrayList<Movie>();
        Label[] movieNames = { movieName1, movieName2, movieName3, movieName4, movieName5, movieName6 };
        HBox[] movieTagBoxs = { movieTagBox1, movieTagBox2, movieTagBox3, movieTagBox4, movieTagBox5, movieTagBox6 };
        ImageView[] movieImages = { movieImage1, movieImage2, movieImage3, movieImage4, movieImage5, movieImage6 };
        Button[] movieButtons = { movieButton1, movieButton2, movieButton3, movieButton4, movieButton5, movieButton6 };

        if (adventureCheckBox.isSelected())
            genres.add("12");
        if (dramaCheckBox.isSelected())
            genres.add("18");
        if (actionCheckBox.isSelected())
            genres.add("28");
        if (animationCheckBox.isSelected())
            genres.add("16");
        if (historyCheckBox.isSelected())
            genres.add("36");
        if (comedyCheckBox.isSelected())
            genres.add("35");
        if (fantasyCheckBox.isSelected())
            genres.add("14");
        if (familyCheckBox.isSelected())
            genres.add("10751");
        if (horrorCheckBox.isSelected())
            genres.add("27");
        if (mysteryCheckBox.isSelected())
            genres.add("9648");
        if (thrillerCheckBox.isSelected())
            genres.add("53");
        if (warCheckBox.isSelected())
            genres.add("10752");
        if (romanceCheckBox.isSelected())
            genres.add("10749");
        if (crimeCheckBox.isSelected())
            genres.add("80");
        if (tvmovieCheckBox.isSelected())
            genres.add("10770");
        if (scifiCheckBox.isSelected())
            genres.add("878");
        if (documentaryCheckBox.isSelected())
            genres.add("99");
        if (westernCheckBox.isSelected())
            genres.add("37");
        if (musicCheckBox.isSelected())
            genres.add("10402");

        if (genres.size() == 0) {
            recommendMessageLabel.setText("請至少選擇1個類型");
            return;
        }

        Loading loading = new Loading(stage);
        loading.show();

        new Thread(() -> {
            try {
                ArrayList<Movie> recommendMovies100 = Service.recommendWithGenres(genres);
                int total_size = recommendMovies100.size();
                if (total_size == 0) {
                    JOptionPane.showMessageDialog(null, "所查詢的類型無資料，請更改條件後再試一次！", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    throw new IndexOutOfBoundsException();
                }

                Platform.runLater(() -> {

                    ArrayList<Movie> recommendMovies = new ArrayList<Movie>();
                    if (total_size <= 6) {
                        for (int i = 0; i < total_size; i++) {
                            recommendMovies.add(recommendMovies100.get(i));
                        }
                    }
                    else {
                        while (recommendMovies.size() <= Math.min(6, total_size)) {
                            int randomNum = (int) (Math.random() * (total_size - 1));
                            if (!recommendMovies.contains(recommendMovies100.get(randomNum))) {
                                recommendMovies.add(recommendMovies100.get(randomNum));
                            }
                        }
                    }
                    for (int i = 0; i < Math.min(recommendMovies.size(), 6); i++) {
                        movieNames[i].setText(recommendMovies.get(i).getTitle());

                        movieTagBoxs[i].getChildren().clear();
                        for (int j = 0; j < Math.min(recommendMovies.get(i).getGenres().size(), 3); j++) {
                            Label tag = new Label(recommendMovies.get(i).getGenres().get(j));
                            tag.setTextFill(Color.WHITE);
                            tag.setId("movieTag");
                            movieTagBoxs[i].getChildren().add(tag);
                        }

                        Image img = new Image(recommendMovies.get(i).getPosterPath());
                        movieImages[i].setImage(img);

                        movieIDs[i] = Integer.toString(recommendMovies.get(i).getID());
                        movieButtons[i].setDisable(false);
                    }
                    for (int i = recommendMovies.size(); i < 6; i++) {
                        movieNames[i].setText("");
                        for (int j = 0; j < 3; j++) {
                            movieTagBoxs[i].getChildren().clear();
                        }
                        movieImages[i].setImage(new Image("file:/resources/images/empty.jpg"));
                        movieIDs[i] = "";
                        movieButtons[i].setDisable(true);
                    }

                    recommendMessageLabel.setText("");

                    loading.closeStage();
                });
            }
            catch (JSONException e) {
                System.err.println("JSON exception: " + e);
                loading.closeStage();
            }
            catch (IOException e) {
                System.err.println("IO exception: " + e);
                loading.closeStage();
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("Index Out Of Bounds Exception!! " + e);
                loading.closeStage();
            }
        }).start();
    }

    public void accountButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try {
                    Scene favoriteScene = SceneManager.switchScene("favorite", stage);
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

    public void movieButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (event.getSource() == movieButton1)
            movieID = movieIDs[0];
        else if (event.getSource() == movieButton2)
            movieID = movieIDs[1];
        else if (event.getSource() == movieButton3)
            movieID = movieIDs[2];
        else if (event.getSource() == movieButton4)
            movieID = movieIDs[3];
        else if (event.getSource() == movieButton5)
            movieID = movieIDs[4];
        else if (event.getSource() == movieButton6)
            movieID = movieIDs[5];

        Platform.runLater(() -> {
            Loading loading = new Loading(stage);
            loading.show();

            new Thread(() -> {
                try {
                    FXMLLoader introductionLoader = new FXMLLoader(
                            getClass().getResource("/resources/fxml/Introduction.fxml"));
                    Parent root = introductionLoader.load();
                    Introduction introductionController = introductionLoader.getController();

                    introductionController.setMovieID(movieID);
                    introductionController.loadMovie();

                    Scene introductionScene = new Scene(root);
                    String introductionCSS = this.getClass().getResource("/resources/css/Introduction.css")
                            .toExternalForm();
                    introductionScene.getStylesheets().add(introductionCSS);

                    Platform.runLater(() -> {
                        stage.setScene(introductionScene);
                        loading.closeStage();
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void loadMovie() throws IOException {
        Label[] movieNames = { movieName1, movieName2, movieName3, movieName4, movieName5, movieName6 };
        HBox[] movieTagBoxs = { movieTagBox1, movieTagBox2, movieTagBox3, movieTagBox4, movieTagBox5, movieTagBox6 };
        ImageView[] movieImages = { movieImage1, movieImage2, movieImage3, movieImage4, movieImage5, movieImage6 };
        Button[] movieButtons = { movieButton1, movieButton2, movieButton3, movieButton4, movieButton5, movieButton6 };

        ArrayList<Movie> recommendMovies20 = Service.recommendWithFavorite();

        int total_size = recommendMovies20.size();
        System.out.println("total size: " + total_size);
        ArrayList<Movie> recommendMovies = new ArrayList<Movie>();
        while (recommendMovies.size() <= 6) {
            int randomNum = (int) (Math.random() * (total_size - 1));
            if (!recommendMovies.contains(recommendMovies20.get(randomNum))) {
                recommendMovies.add(recommendMovies20.get(randomNum));
            }
        }
        for (int i = 0; i < Math.min(recommendMovies.size(), 6); i++) {
            movieNames[i].setText(recommendMovies.get(i).getTitle());

            movieTagBoxs[i].getChildren().clear();
            for (int j = 0; j < Math.min(recommendMovies.get(i).getGenres().size(), 3); j++) {
                Label tag = new Label(recommendMovies.get(i).getGenres().get(j));
                tag.setTextFill(Color.WHITE);
                movieTagBoxs[i].getChildren().add(tag);
            }

            Image img = new Image(recommendMovies.get(i).getPosterPath());
            movieImages[i].setImage(img);

            movieIDs[i] = Integer.toString(recommendMovies.get(i).getID());
            movieButtons[i].setDisable(false);
        }
        for (int i = recommendMovies.size(); i < 6; i++) {
            movieNames[i].setText("");
            movieTagBoxs[i].getChildren().clear();
            movieImages[i].setImage(new Image("file:/resources/images/empty.jpg"));
            movieIDs[i] = "";
            movieButtons[i].setDisable(true);
        }

        recommendMessageLabel.setText("");

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
