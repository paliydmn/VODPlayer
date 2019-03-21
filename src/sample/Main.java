package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static Stage stage;

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getStylesheets().add(String.valueOf(this.getClass().getResource("textArea.css")));

        stage.setTitle("VOD Player");
        stage.setScene(new Scene(root, 650, 405));
        stage.show();


       // reSizeListener(stage);

    }

public static void reSizeListener(Stage stage){
    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
            System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());

    stage.widthProperty().addListener(stageSizeListener);
    stage.heightProperty().addListener(stageSizeListener);
}

public static void setNewHeight(Double h){
    stage.setHeight(h);
}

public static void main(String[] args) {
        launch(args);
    }
}
