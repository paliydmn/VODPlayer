package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class Controller {
    public MediaView mediaView;
    public ListView listView;
    public TextField txtFieldToTime;
    public Slider slider;
    public Label lblCurretTime;
    public Label lblSelected;
    MediaPlayer mp;
    Media media;
    @FXML
    public void initialize() {
        ObservableList<String> channels = FXCollections.observableArrayList();
        channels.add("https://archive.org/download/AlmostChristmas20161080pRemux/Almost%20Christmas%20(2016)%201080p%20Remux.mp4");
        channels.add("https://ia801507.us.archive.org/18/items/viral_201709/viral.mp4");
        channels.add("https://archive.org/download/Home.2015.720p.YIFYTopArchive.IR/Home.2015.720p.YIFY_TopArchive.IR.mp4");
        channels.add("http://www.deadlyblogger.com/NewRelease/needforspeed.mp4");
        channels.add("https://bobmovies.net/hl/Whiplash_2014_720p.m3u8");
        channels.add("https://archive.org/download/PenguinsOfMadagascar2014720pBrRipX264YIFY/Penguins%20of%20Madagascar%20(2014)%20720p%20BrRip%20x264%20-%20YIFY.mp4");
        listView.setItems(channels);


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue);
                
                lblSelected.setText(newValue);
                media = new Media(newValue);
                if(mp !=null)
                mp.stop();

                mp = new MediaPlayer(media);
                mediaView.setMediaPlayer(mp);
                mp.setAutoPlay(true);
            }
        });

        slider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(slider.getValue());
                mp.seek(Duration.minutes(slider.getValue()));
                lblCurretTime.setText(String.format("%.2f",slider.getValue()));
            }
        });

        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();

        width.bind(javafx.beans.binding.Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(javafx.beans.binding.Bindings.selectDouble(mediaView.sceneProperty(), "height"));

    }

    public void onPlay(ActionEvent actionEvent) {
        mp.play(); // 4

    }

    public void onStop(ActionEvent actionEvent) {
        mp.stop();
    }

    public void onNext(ActionEvent actionEvent) {
        MediaPlayer.Status currentStatus = mp.getStatus();

        System.out.println(mp.getTotalDuration());

        mp.seek( mp.getCurrentTime().add(new Duration(20000)));
        System.out.println("Current time: " + mp.getCurrentTime());
    }

    public void onPause(ActionEvent actionEvent) {
        mp.pause();
        slider.setMax(mp.getTotalDuration().toMinutes());

    }

    public void onVolumeUP(ActionEvent actionEvent) {
        double vol = mp.getVolume();
        mp.setVolume(vol+0.1);
        System.out.println(mp.currentRateProperty());
    }

    public void onVolumeDown(ActionEvent actionEvent) {
        double vol = mp.getVolume();
        mp.setVolume(vol-0.2);
    }

    public void onToTime(ActionEvent actionEvent) {
        if(txtFieldToTime != null && !txtFieldToTime.getText().equals("")){
            double d = Double.parseDouble(txtFieldToTime.getText());
            mp.seek(new Duration(d));
        }
    }
}
