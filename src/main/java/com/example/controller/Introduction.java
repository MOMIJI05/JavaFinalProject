package com.example.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
    private Label movieName, Scipt;
    @FXML
    private ImageView movieImage;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private VBox commentsContainer;
    @FXML
    private FlowPane movieTagBox;

    private String initialDownloadPath = System.getenv("USERPROFILE") + "\\Downloads"; // 預設的user下載位置
    private String fullStar = "M12 17.27 18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z";
    private String emptyStar = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
    private String movieID = "";
    private Movie movie;

    public void loadMovie() throws IOException {
        try {
            movie = Service.getMovieDetail(movieID);
            movieName.setText(movie.getTitle());

            movieTagBox.getChildren().clear();
            List<String> tags = movie.getGenres();
            for (int i = 0; i < tags.size(); i++) {
                Label tag = new Label(tags.get(i));
                tag.setTextFill(Color.WHITE);
                movieTagBox.getChildren().add(tag);
            }
            movieImage.setImage(new Image(movie.getPosterPath()));
            Scipt.setText(movie.getOverview());
            if (Service.checkIfFavorite(movieID)) {
                starSVGPath.setContent(fullStar);
            }
            else {
                starSVGPath.setContent(emptyStar);
            }
            hyperlink.setText(movie.getLink());

            ArrayList<String> comments = Service.getComment(movieID);
            commentsContainer.getChildren().clear();

            Label commentLabel = new Label("Comment");
            commentLabel.setId("line");
            commentsContainer.getChildren().add(commentLabel);
            for (int i = 0; i < comments.size(); i++) {
                TitledPane titledPane = new TitledPane();
                Label label = new Label(comments.get(i)), line = new Label("\n----------------------------\n");
                label.setId("comment");
                label.setWrapText(true);
                label.setMaxWidth(230);
                line.setId("line");

                titledPane.setContent(label);
                titledPane.setExpanded(false);

                commentsContainer.getChildren().add(titledPane);
                commentsContainer.getChildren().add(line);
            }
        }
        catch (NullPointerException e) {
            System.err.println("Null Pointer Exception!!");
        }
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
                    recommendController.loadMovie();
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

    public void starButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Loading loading = new Loading(stage);
        loading.show();

        Boolean currentIsEmptyStar = starSVGPath.getContent().equals(emptyStar);

        new Thread(() -> {
            try {
                if (currentIsEmptyStar) {
                    Service.addFavorite(movieID);
                }
                else {
                    Service.removeFavorite(movieID);
                }

                Platform.runLater(() -> {
                    starSVGPath.setContent(currentIsEmptyStar ? fullStar : emptyStar);
                    loading.closeStage();
                });
            }
            catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    loading.closeStage();
                });
            }
        }).start();
    }

    public void downloadButtonOnAction(ActionEvent event) throws IOException, URISyntaxException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Movie movie = Service.getMovieDetail(movieID);
        String dafaultFilename = movie.getTitle().replaceAll("[<>:\"/\\\\|?*]", "") + "_海報.jpg";

        FileChooser chooser = new FileChooser();
        chooser.setTitle("選擇下載路徑");
        chooser.setInitialDirectory(new File(initialDownloadPath));
        chooser.setInitialFileName(dafaultFilename);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Image (*.jpg)", "*.jpg"));

        File path = chooser.showSaveDialog(stage);

        Loading loading = new Loading(stage);
        loading.show();

        new Thread(() -> {
            try {

                Service.downloadPoster(path.toString(), movie.getPosterPath());

                Platform.runLater(() -> {
                    loading.closeStage();
                });
            }
            catch (Exception e) {
                System.err.println(e);
                loading.closeStage();
            }
        }).start();
    }

    public void linkClickOnAction(ActionEvent event) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(hyperlink.getText()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }
}
