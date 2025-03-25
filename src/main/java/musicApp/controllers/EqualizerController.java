package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.models.Equalizer;
import musicApp.views.EqualizerView;

import java.util.List;

public class EqualizerController extends ViewController<EqualizerView, EqualizerController>{
    private final SettingsController settingsController;
    private final Stage stage;
    private final Equalizer equalizer;

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

    public void updateEqualizerBand(int bandIndex, double value){
        this.equalizer.setBandGain(bandIndex, value);
    }

    public int getBandFrequency(int bandIndex){
        return this.equalizer.getBandFrequency(bandIndex);
    }

    public double getEqualizerBandGain(int bandIndex) {
        return this.equalizer.getBandGain(bandIndex);
    }

    public double getMaxGainDB() {
        return this.equalizer.getMaxGainDB();
    }

    public double getMinGainDB() {
        return this.equalizer.getMinGainDB();
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