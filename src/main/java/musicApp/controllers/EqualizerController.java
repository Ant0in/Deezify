package musicApp.controllers;

import javafx.stage.Stage;
import musicApp.views.EqualizerView;

public class EqualizerController extends ViewController<EqualizerView, EqualizerController>{
    private final SettingsController settingsController;
    private final Stage stage;

    public static final double MAX_EQUALIZER_GAIN_DB = 10.0;
    public static final double MIN_EQUALIZER_GAIN_DB = -10.0;
    private final int[] bandFrequencies = {32, 64, 125, 250, 500, 1000, 2000, 4000, 8000, 16000};

    public EqualizerController(SettingsController settingsController) {
        super(new EqualizerView());
        this.settingsController = settingsController;
        this.stage = new Stage();
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
        settingsController.updateEqualizerBand(bandIndex, value);
    }

    public int getBandFrequency(int bandIndex){
        return bandFrequencies[bandIndex];
    }
}
