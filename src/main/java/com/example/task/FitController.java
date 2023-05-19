package com.example.task;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;

public class FitController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button openButton;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider seekSlider;

    @FXML
    private Slider volumeSlider;

    private MediaPlayer mediaPlayer;

    @FXML
    private void initialize() {
        seekSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null && seekSlider.isValueChanging()) {
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(newValue.doubleValue() / 100.0));
            }
        });

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume((100 - newValue.doubleValue()) / 100.0);
            }
        });
    }

    @FXML
    private void openVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.avi"));
        File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if (selectedFile != null) {
            Media media = new Media(selectedFile.toURI().toString());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnReady(() -> {
                double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
                seekSlider.setMin(0);
                seekSlider.setMax(100);
                seekSlider.setValue(0);
                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                    if (!seekSlider.isValueChanging()) {
                        seekSlider.setValue(newValue.toSeconds() / totalDuration * 100);
                    }
                });
            });
        }
    }

    @FXML
    private void playPauseVideo() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    @FXML
    private void seekVideoStarted() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void seekVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(seekSlider.getValue() / 100.0));
        }
    }

    @FXML
    private void seekVideoFinished() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void adjustVolume() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume((100 - volumeSlider.getValue()) / 100.0);
        }
    }
}
