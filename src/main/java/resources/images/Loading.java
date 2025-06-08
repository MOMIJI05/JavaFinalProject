package resources.images;

import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Loading {
    private Stage stage;
    private StackPane root;

    public Loading(Stage owner){
        ImageView loadingImageView = new ImageView(new Image("/resources/images/oc loading icon GIF.gif"));
        loadingImageView.setFitHeight(350);
        loadingImageView.setPreserveRatio(true);

        root = new StackPane();
        root.setMouseTransparent(true);
        root.setPrefSize(150, 100);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.3), null, null)));
        root.getChildren().addAll(loadingImageView);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(owner);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setX(owner.getX());
        stage.setY(owner.getY());
        stage.setHeight(owner.getHeight());
        stage.setWidth(owner.getWidth());
    }

    public void show(){
        Platform.runLater(() -> stage.show());
    }

    public void closeStage(){
        Platform.runLater(() -> stage.close());
    }

}
