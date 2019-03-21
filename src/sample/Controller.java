package sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    public MediaView mediaView;
    public ListView listView;
    public TextField txtFieldToTime;
    public Slider slider;
    public Label lblSelected;


    public Label lblCurretTimeSec;

    public ProgressIndicator waitingBar;
    public ImageView imgFullScreen;
    public AnchorPane controlPanel;
    public SplitPane splitter;
    public AnchorPane mainAp;
    MediaPlayer mp;
    Media media;


    final int[] i = {1};
    Timer timer ;

    @FXML
    public void initialize() {

        Image refresh = new Image(Controller.class.getResourceAsStream("fullCreen.png"));
        imgFullScreen.setImage(refresh);

        //VideoLibraryItem ch = new VideoLibraryItem("https://archive.org/download/AlmostChristmas20161080pRemux/Almost%20Christmas%20(2016)%201080p%20Remux.mp4", "Almost Christmas");
        ObservableList<VideoLibraryItem> videos = FXCollections.observableArrayList();

        videos.add(new VideoLibraryItem("https://archive.org/download/AlmostChristmas20161080pRemux/Almost%20Christmas%20(2016)%201080p%20Remux.mp4", "Almost Christmas"));
        videos.add(new VideoLibraryItem("https://ia801507.us.archive.org/18/items/viral_201709/viral.mp4", "Viral"));
        videos.add(new VideoLibraryItem("https://archive.org/download/Home.2015.720p.YIFYTopArchive.IR/Home.2015.720p.YIFY_TopArchive.IR.mp4", "Home.2015.720p"));
        videos.add(new VideoLibraryItem("http://www.deadlyblogger.com/NewRelease/needforspeed.mp4", "NeedForSpeed"));
        videos.add(new VideoLibraryItem("https://bobmovies.net/hl/Whiplash_2014_720p.m3u8", "Whiplash"));
        videos.add(new VideoLibraryItem("https://archive.org/download/PenguinsOfMadagascar2014720pBrRipX264YIFY/Penguins%20of%20Madagascar%20(2014)%20720p%20BrRip%20x264%20-%20YIFY.mp4", "Penguins Of Madagascar 2014 720p"));

          File file = new File("D:/Work/MMedia/Хроника/Hronika.2012.Extended.Edition.RUS.BDRip.avi");
          final String MEDIA_URL = file.toURI().toString();
        videos.add(new VideoLibraryItem(MEDIA_URL, "My_Millery"));

        listView.setItems(videos);

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<VideoLibraryItem>() {

            @Override
            public void changed(ObservableValue<? extends VideoLibraryItem> observable, VideoLibraryItem oldValue, VideoLibraryItem newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue);

                lblSelected.setText(newValue.getName());
                media = new Media(newValue.getLink());
                if(mp !=null)
                    mp.stop();

                mp = new MediaPlayer(media);
                mediaView.setMediaPlayer(mp);
                slider.setValue(0);
                lblCurretTimeSec.setText("0");
                onPlay();
            }
    });

        slider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(slider.getValue());
                double seekSec = slider.getValue() * 60;
                mp.seek(Duration.seconds(seekSec));
                long secs = (long) mp.getCurrentTime().toSeconds();
                lblCurretTimeSec.setText(String.format("%02d:%02d:%02d", (secs % 86400) / 3600, (secs % 3600) / 60, secs % 60));
            }
        });

        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();


        width.bind(javafx.beans.binding.Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(javafx.beans.binding.Bindings.selectDouble(mediaView.sceneProperty(), "height"));


        mainAp.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> controlPanel.setVisible(true));




       /* Scene stage = mainAp.getScene();
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
*/
        //JSONParser jsonParser = new JSONParser("")
    }

    public void parseJson(){
        try {
            String contents = new String(Files.readAllBytes(Paths.get("playlist.json")));
        //    Gson g = new Gson();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void onMouseExited(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getEventType());
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);
    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getEventType());
        slider.setShowTickLabels(true);

    }

    public void onMouseEntered(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getEventType());
        slider.setShowTickMarks(true);
    }

    public void onMouseClickedFullScreen(MouseEvent mouseEvent) {
        final DoubleProperty width = mediaView.fitWidthProperty();
        final DoubleProperty height = mediaView.fitHeightProperty();

        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);

        Main.setNewHeight(mediaView.getFitHeight()+90);

        controlPanel.setVisible(false);
        splitter.setDividerPositions(1);
    }

    class VideoLibraryItem {
        String link;
        String name;
         public VideoLibraryItem(String link, String name) {
             this.link = link;
             this.name = name;
         }

         public String getLink() {
             return link;
         }

         public String getName() {
             return name;
         }

         @Override
         public String toString() {
             return name;
         }
     }

    public void onPlay() {
        mp.play(); // 4
        if(timer == null) {
            timer = new Timer();

            waitingBar.setVisible(true);
            //if(timer.)
            //slider.setMax(mp.getTotalDuration().toSeconds());
            timer.schedule(new TimerTask() {
                public void run() {
                    System.out.println(mp.getCurrentTime());
                    // do your work
                    double secDuration = mp.getTotalDuration().toSeconds();
                    double minDuration = mp.getTotalDuration().toMinutes();
                    if (minDuration > 0 && slider.getMax() == 0) {
                        slider.setMax(minDuration);
                        mediaView.fitWidthProperty().bind(media.widthProperty());
                        waitingBar.setVisible(false);
                        Main.setNewHeight(mediaView.getFitHeight()+90);
                       // mainAp.setMaxHeight(mediaView.getFitHeight()+90);
                    }

                    double curPosition = Double.parseDouble(String.valueOf(mp.getCurrentTime().toSeconds()));
                    Platform.runLater(() -> {

                        long secs = (long) curPosition;
                        System.out.println(String.format("%02d:%02d:%02d", (secs % 86400) / 3600, (secs % 3600) / 60, secs % 60));
                        lblCurretTimeSec.setText(String.format("%02d:%02d:%02d", (secs % 86400) / 3600, (secs % 3600) / 60, secs % 60));
                    });
                    System.out.println("CurrTime = " + curPosition);
                    //slider.setValue((mp.getCurrentTime().toSeconds()));
                }
            }, 0, 1000);
        }
    }

    public void onStop(ActionEvent actionEvent) {
        mp.stop();
        timer.cancel();
        timer.purge();
        slider.setValue(0.0);
        lblCurretTimeSec.setText("00");
        waitingBar.setVisible(false);
        timer = null;

        Main.setNewHeight(405.0);

    }

    public void onNext(ActionEvent actionEvent) {
        MediaPlayer.Status currentStatus = mp.getStatus();

        System.out.println(mp.getTotalDuration());

        mp.seek( mp.getCurrentTime().add(new Duration(20000)));
        System.out.println("Current time: " + mp.getCurrentTime());
    }

    public void onPause(ActionEvent actionEvent) {
        mp.pause();
       // slider.setMax(mp.getTotalDuration().toMinutes());
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
            mp.seek(Duration.millis(new Duration(d).toSeconds()));
        }
    }
}
