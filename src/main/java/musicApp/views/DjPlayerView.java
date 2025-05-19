package musicApp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

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

    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }

    public void addListeners() {

        gainSlider.valueProperty().addListener((_, _, newVal) -> listener.handleChangeGainMode(newVal.doubleValue()));

        pressureSlider.valueProperty().addListener((_, _, newVal) -> listener.handleChangePressureStrength(newVal.doubleValue()));

        bassBoostSlider.valueProperty().addListener((_, _, newVal) -> listener.handleChangeBassBoostGain(newVal.doubleValue()));

        waveSlider.valueProperty().addListener((_, _, newVal) -> updateWaveSpeed(newVal.doubleValue()));
    }

    private void updateWaveSpeed(double value) {
        listener.handleChangeWaveSpeed(value);
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

    /**
     * Listener interface used to delegate actions from the view to the controller logic.
     */
    public interface DjPlayerViewListener {
        void handleClose();

        void handleChangeGainMode(double gain);

        void handleChangePressureStrength(double strength);

        void handleChangeBassBoostGain(double gain);

        void handleChangeWaveSpeed(double speed);
    }

}
