package musicApp.controllers;

import javafx.scene.media.EqualizerBand;
import javafx.stage.Stage;
import musicApp.models.Settings;
import musicApp.views.EqualizerView;

import java.util.List;
import java.util.Set;

public class EqualizerController extends ViewController<EqualizerView, EqualizerController>{
    private final SettingsController settingsController;
    private final Stage stage;
    private final List<Double> equalizerBands;

    public static final double USER_MAX_GAIN_DB = 20.0;
    public static final double USER_MIN_GAIN_DB = -20.0;

    private final int[] bandFrequencies = {32, 64, 125, 250, 500, 1000, 2000, 4000, 8000, 16000};

    public EqualizerController(SettingsController settingsController, List<Double> equalizerBands) {
        super(new EqualizerView());
        this.settingsController = settingsController;
        this.stage = new Stage();
        this.equalizerBands = equalizerBands;
        initView("/fxml/Equalizer.fxml");
    }

    public void show(){
        this.view.show(stage);
    }

    public void close(){
        stage.close();
        this.settingsController.show();
    }

    private double mapUserGainToJavaFX(double userGain) {
        return ((userGain - USER_MIN_GAIN_DB) / (USER_MAX_GAIN_DB - USER_MIN_GAIN_DB)) *
                (Settings.MAX_GAIN_DB - Settings.MIN_GAIN_DB) + Settings.MIN_GAIN_DB;
    }

    public void updateEqualizerBand(int bandIndex, double value){
        double javaFxValue =  mapUserGainToJavaFX(value);
        equalizerBands.set(bandIndex, javaFxValue);
        settingsController.updateEqualizerBand(bandIndex, javaFxValue);
    }

    public int getBandFrequency(int bandIndex){
        return bandFrequencies[bandIndex];
    }

    private double mapJavaFXToUserGain(double javaFxValue) {
        return ((javaFxValue - Settings.MIN_GAIN_DB) / (Settings.MAX_GAIN_DB - Settings.MIN_GAIN_DB)) *
                (USER_MAX_GAIN_DB - USER_MIN_GAIN_DB) + USER_MIN_GAIN_DB;
    }

    public double getEqualizerBandGain(int bandIndex) {
        double javaFxValue = equalizerBands.get(bandIndex);
        return mapJavaFXToUserGain(javaFxValue);
    }
}
