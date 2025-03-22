package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.models.Equalizer;
import musicApp.views.EqualizerView;

import java.util.List;

public class EqualizerController extends ViewController<EqualizerView, EqualizerController>{
    private final SettingsController settingsController;
    private final Stage stage;
    private final Equalizer equalizer;

    public static final double USER_MAX_GAIN_DB = 20.0;
    public static final double USER_MIN_GAIN_DB = -20.0;


    public EqualizerController(SettingsController settingsController, Equalizer equalizer) {
        super(new EqualizerView());
        this.settingsController = settingsController;
        this.stage = new Stage();
        this.equalizer = equalizer;
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
                (this.equalizer.getMaxGainDB() - this.equalizer.getMinGainDB()) + this.equalizer.getMinGainDB();
    }

    public void updateEqualizerBand(int bandIndex, double value){
        double javaFxValue =  mapUserGainToJavaFX(value);
        this.equalizer.setEqualizerBand(bandIndex, javaFxValue);
    }

    public int getBandFrequency(int bandIndex){
        return this.equalizer.getBandFrequency(bandIndex);
    }

    private double mapJavaFXToUserGain(double javaFxValue) {
        return ((javaFxValue - this.equalizer.getMinGainDB()) / (this.equalizer.getMaxGainDB() - this.equalizer.getMinGainDB())) *
                (USER_MAX_GAIN_DB - USER_MIN_GAIN_DB) + USER_MIN_GAIN_DB;
    }

    public double getEqualizerBandGain(int bandIndex) {
        double javaFxValue = this.equalizer.getEqualizerBand(bandIndex);
        return mapJavaFXToUserGain(javaFxValue);
    }

    public void update() {
        List<Double> equalizerBands = this.view.getSlidersValues();
        for(int bandIndex = 0; bandIndex < equalizerBands.size(); bandIndex++){
            updateEqualizerBand(bandIndex, equalizerBands.get(bandIndex));
        }
        this.view.updateSlidersValues();
    }

    public void handleCancel() {
        this.view.updateSlidersValues();
    }

}
