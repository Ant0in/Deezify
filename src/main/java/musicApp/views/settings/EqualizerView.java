package musicApp.views.settings;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import musicApp.controllers.settings.EqualizerController;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.util.List;

public class EqualizerView extends View<EqualizerView, EqualizerController> {


    @FXML
    private Button okButton, cancelButton;
    @FXML
    private HBox slidersContainer;

    @Override
    public void init() {
        initButtons();
        initTranslations();
        initSliders();
    }

    public void show(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Equalizer");
        stage.show();
        stage.setOnCloseRequest(event -> {
            viewController.close();
        });
    }


    private void initSliders() {
        int numberOfSliders = 10;
        for (int i = 0; i < numberOfSliders; i++) {
            VBox sliderVBox = sliderVBox(i);
            slidersContainer.getChildren().add(sliderVBox);
        }
    }

    private VBox sliderVBox(int sliderIndex) {
        int padding = 15;
        VBox sliderVBox = new VBox(10);
        sliderVBox.setAlignment(Pos.CENTER);
        sliderVBox.setPadding(new javafx.geometry.Insets(padding, padding, padding, padding));
        Slider slider = Slider(sliderIndex);
        Label sliderValueLabel = sliderValueLabel(slider);
        Label freqLabel = bandFrequenciesLabel(sliderIndex);
        sliderVBox.getChildren().addAll(sliderValueLabel, slider, freqLabel);
        return sliderVBox;
    }

    private Slider Slider(int sliderIndex) {
        double initGainValue = viewController.getEqualizerBandGain(sliderIndex);
        Slider slider = new Slider(viewController.getMinGainDB(), viewController.getMaxGainDB(), initGainValue);
        slider.setOrientation(javafx.geometry.Orientation.VERTICAL);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(5);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        return slider;
    }

    private Label sliderValueLabel(Slider slider) {
        Label label = new Label();
        label.setMinWidth(50);
        label.setAlignment(Pos.CENTER);
        label.textProperty().bind(slider.valueProperty().asString("%.0f dB"));
        return label;
    }

    private Label bandFrequenciesLabel(int bandIndex) {
        Label label = new Label();
        int frequency = viewController.getBandFrequency(bandIndex);
        String freqLabelString = getFreqLabelString(frequency);
        label.setText(freqLabelString);
        return label;
    }

    private String getFreqLabelString(int value) {
        String freq = "Hz";
        if (value >= 1000) {
            value /= 1000;
            freq = "KHz";
        }
        return value + " " + freq;
    }

    private void initTranslations() {
        LanguageManager lm = LanguageManager.getInstance();
        okButton.textProperty().bind(Bindings.createStringBinding(
                () -> lm.get("settings.ok"), lm.getLanguageProperty()
        ));
        cancelButton.textProperty().bind(Bindings.createStringBinding(
                () -> lm.get("button.cancel"), lm.getLanguageProperty()
        ));
    }

    private void initButtons() {
        okButton.setOnMouseClicked(_ -> viewController.close());
        cancelButton.setOnMouseClicked(_ -> viewController.handleCancel());
    }

    public List<Double> getSlidersValues() {
        return slidersContainer.getChildren().stream()
                .filter(node -> node instanceof VBox)
                .map(vBox -> (VBox) vBox)
                .map(vBox -> vBox.getChildren().stream()
                        .filter(child -> child instanceof Slider)
                        .map(child -> ((Slider) child).getValue())
                        .findFirst()
                        .orElse(0.0))
                .toList();
    }

    public void updateSlidersValues() {
        int sliderIndex = 0;

        for (javafx.scene.Node node : slidersContainer.getChildren()) {
            if (node instanceof VBox vBox) {
                for (javafx.scene.Node child : vBox.getChildren()) {
                    if (child instanceof Slider slider) {
                        double initGainValue = viewController.getEqualizerBandGain(sliderIndex);
                        slider.setValue(initGainValue);
                        sliderIndex++;
                        break;
                    }
                }
            }
        }
    }

}
