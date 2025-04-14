package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import musicApp.controllers.DjPlayerController;


public class DjPlayerView extends View<DjPlayerView, DjPlayerController> {

    @FXML
    private Button playButton, pauseButton;
    @FXML
    private Button gainButton, pressureButton, bassBoostButton, waveButton;
    @FXML
    private Slider gainSlider, bassBoostSlider, pressureSlider, waveSlider;
    @FXML
    private Label gainLabel, bassBoostLabel, pressureLabel, speedLabel;

    public DjPlayerView() {
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void init() {
        configureComponents();
        setOnAction();
        addListeners();
    }

    public void configureComponents() {
        //
    }

    public void addListeners() {

        gainLabel.setText("\ud83c\udf9b");
        bassBoostLabel.setText("\ud83d\udcc8");
        speedLabel.setText("x0");
        pressureLabel.setText("\ud83d\udca7");


        gainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            toggleBoostGainMode(newVal.doubleValue());
        });

        pressureSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            togglePressureMode(newVal.doubleValue());
        });

        bassBoostSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            toggleBassBoostMode(newVal.doubleValue());
        });

        waveSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            toggleWaveMode(newVal.doubleValue());
            double value = newVal.doubleValue();
            speedLabel.setText("x0");
            if (value >= 33) {
                speedLabel.setText("x1");
            }
            if (value >= 66) {
                speedLabel.setText("x2");
            }
            if (value >= 99) {
                speedLabel.setText("x3");
            }
        });
    }

    public void setOnAction() {
        playButton.setOnAction(_ -> viewController.unpause());
        pauseButton.setOnAction(_ -> viewController.pause());
        gainButton.setOnAction(_ -> viewController.toggleBoostGainMode());
        pressureButton.setOnAction(_ -> viewController.togglePressureMode());
        bassBoostButton.setOnAction(_ -> viewController.toggleWaveGainMode());
    }

    private void toggleBoostGainMode(double value) {
        viewController.changeGainMode(value);
    }

    private void togglePressureMode(double value) {
        viewController.changePressureStrength(value);
    }

    private void toggleBassBoostMode(double value) {
        viewController.changeBassBoostGain(value);
    }

    private void toggleWaveMode(double value) {
        viewController.changeWaveSpeed(value);
    }

}
