package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import musicApp.exceptions.EqualizerGainException;

public class DjPlayerView extends View {
    
    private DjPlayerViewListener listener;
    private Stage stage;

    @FXML
    private Slider gainSlider, bassBoostSlider, pressureSlider, waveSlider;
    @FXML
    private Label gainLabel, bassBoostLabel, pressureLabel, speedLabel;

    public DjPlayerView() {
    }

    /**
     * Listener interface used to delegate actions from the view to the controller logic.
     */
    public interface DjPlayerViewListener {
        void handleClose();

        void changeGainMode (double gain);

        void changePressureStrength(double strength);

        void changeBassBoostGain(double gain);

        void changeWaveSpeed(double speed);
    }

    /**
     * Sets listener.
     *
     * @param newListener the listener
     */
    public void setListener(DjPlayerViewListener newListener) {
        listener = newListener;
    }

    @Override
    public void init() {
        addListeners();
        initStage();
    }

    public void resetEffects() {
        gainSlider.setValue(0);
        bassBoostSlider.setValue(0);
        pressureSlider.setValue(0);
        waveSlider.setValue(0);
    }

    private void initStage() {
        stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(_ -> listener.handleClose());
        stage.setTitle("DJ Player");
        stage.setResizable(false);
    }

    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }

    public void addListeners() {

        gainLabel.setText("\ud83c\udf9b");
        bassBoostLabel.setText("\ud83d\udcc8");
        speedLabel.setText("x0");
        pressureLabel.setText("\ud83d\udca7");


        gainSlider.valueProperty().addListener((_, _, newVal) -> listener.changeGainMode(newVal.doubleValue()));

        pressureSlider.valueProperty().addListener((_, _, newVal) -> listener.changePressureStrength(newVal.doubleValue()));

        bassBoostSlider.valueProperty().addListener((_, _, newVal) -> listener.changeBassBoostGain(newVal.doubleValue()));

        waveSlider.valueProperty().addListener((_, _, newVal) -> updateWaveSpeed(newVal.doubleValue()));
    }

    private void updateWaveSpeed(double value) {
        listener.changeWaveSpeed(value);
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
    }

}
