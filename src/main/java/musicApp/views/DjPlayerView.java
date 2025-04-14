package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import musicApp.controllers.DjPlayerController;
import musicApp.exceptions.EqualizerGainException;


public class DjPlayerView extends View<DjPlayerView, DjPlayerController> {

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
        addListeners();
    }

    public void addListeners() {

        gainLabel.setText("\ud83c\udf9b");
        bassBoostLabel.setText("\ud83d\udcc8");
        speedLabel.setText("x0");
        pressureLabel.setText("\ud83d\udca7");


        gainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                toggleBoostGainMode(newVal.doubleValue());
            } catch (EqualizerGainException e) {
                e.printStackTrace();
            }
        });

        pressureSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                togglePressureMode(newVal.doubleValue());
            } catch (EqualizerGainException e) {
                e.printStackTrace();
            }
        });

        bassBoostSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                toggleBassBoostMode(newVal.doubleValue());
            } catch (EqualizerGainException e) {
                e.printStackTrace();
            }
        });

        waveSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                toggleWaveMode(newVal.doubleValue());
            } catch (EqualizerGainException e) {
                e.printStackTrace();
            }
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

    private void toggleBoostGainMode(double value) throws EqualizerGainException {
        viewController.changeGainMode(value);
    }

    private void togglePressureMode(double value) throws EqualizerGainException {
        viewController.changePressureStrength(value);
    }

    private void toggleBassBoostMode(double value) throws EqualizerGainException {
        viewController.changeBassBoostGain(value);
    }

    private void toggleWaveMode(double value) throws EqualizerGainException {
        viewController.changeWaveSpeed(value);
    }

}
