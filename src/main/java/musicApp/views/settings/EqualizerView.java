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
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.util.List;

/**
 * This is the view for the Equalizer settings.
 * It contains sliders for each band of the equalizer.
 */
public class EqualizerView extends View {

    private EqualizerViewListener listener;

    @FXML
    private Button okButton, cancelButton;
    @FXML
    private HBox slidersContainer;

    /**
     * Listener interface for handling events and retrieving configuration data for the equalizer.
     * Implement this interface to provide the necessary actions and configuration for each equalizer band.
     */
    public interface EqualizerViewListener {
        void close();

        void handleCancel();

        int getBandFrequency(int bandIndex);

        double getMaxGainDB();

        double getMinGainDB();

        double getEqualizerBandGain(int bandIndex);
    }

    /**
     * Sets listener.
     *
     * @param _listener the listener
     */
    public void setListener(EqualizerView.EqualizerViewListener _listener) {
        listener = _listener;
    }

    /**
     * Initializes the equalizer view.
     * This method sets up the buttons, translations, and sliders for the equalizer bands.
     */
    @Override
    public void init() {
        initButtons();
        initTranslations();
        initSliders();
    }

    /**
     * Show the equalizer view.
     *
     * @param stage The stage to show the view on.
     */
    public void show(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Equalizer");
        stage.show();
        stage.setOnCloseRequest(event -> {
            listener.close();
        });
    }

    /**
     * Initialize the sliders for the equalizer bands.
     * Each slider is created with a VBox containing the slider, its value label, and frequency label.
     */
    private void initSliders() {
        int numberOfSliders = 10;
        for (int i = 0; i < numberOfSliders; i++) {
            VBox sliderVBox = sliderVBox(i);
            slidersContainer.getChildren().add(sliderVBox);
        }
    }

    /**
     * Create a VBox containing a slider, its value label, and frequency label.
     *
     * @param sliderIndex The index of the slider.
     * @return A VBox containing the slider and its labels.
     */
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

    /**
     * Create a slider for the equalizer band.
     *
     * @param sliderIndex The index of the slider.
     * @return A Slider object for the equalizer band.
     */
    private Slider Slider(int sliderIndex) {
        double initGainValue = listener.getEqualizerBandGain(sliderIndex);
        Slider slider = new Slider(listener.getMinGainDB(), listener.getMaxGainDB(), initGainValue);
        slider.setOrientation(javafx.geometry.Orientation.VERTICAL);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(5);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        return slider;
    }

    /**
     * Create a label for the slider value.
     *
     * @param slider The slider to bind the label to.
     * @return A Label object for the slider value.
     */
    private Label sliderValueLabel(Slider slider) {
        Label label = new Label();
        label.setMinWidth(50);
        label.setAlignment(Pos.CENTER);
        label.textProperty().bind(slider.valueProperty().asString("%.0f dB"));
        return label;
    }

    /**
     * Create a label for the band frequency.
     *
     * @param bandIndex The index of the band.
     * @return A Label object for the band frequency.
     */
    private Label bandFrequenciesLabel(int bandIndex) {
        Label label = new Label();
        int frequency = listener.getBandFrequency(bandIndex);
        String freqLabelString = getFreqLabelString(frequency);
        label.setText(freqLabelString);
        return label;
    }

    /**
     * Get the frequency label string based on the value.
     *
     * @param value The frequency value.
     * @return A string representing the frequency label.
     */
    private String getFreqLabelString(int value) {
        String freq = "Hz";
        if (value >= 1000) {
            value /= 1000;
            freq = "KHz";
        }
        return value + " " + freq;
    }

    /**
     * Initialize the language combobox to display the current language.
     */
    private void initTranslations() {
        LanguageManager lm = LanguageManager.getInstance();
        okButton.textProperty().bind(Bindings.createStringBinding(
                () -> lm.get("settings.ok"), lm.getLanguageProperty()
        ));
        cancelButton.textProperty().bind(Bindings.createStringBinding(
                () -> lm.get("button.cancel"), lm.getLanguageProperty()
        ));
    }

    /**
     * Initialize the buttons in the view.
     */
    private void initButtons() {
        okButton.setOnMouseClicked(_ -> listener.close());
        cancelButton.setOnMouseClicked(_ -> listener.handleCancel());
    }

    /**
     * Get the values of the sliders in the equalizer view.
     *
     * @return A list of doubles representing the values of the sliders.
     */
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

    /**
     * Update the values of the sliders in the equalizer view.
     * This method sets the value of each slider to the corresponding value from the equalizer controller.
     */
    public void updateSlidersValues() {
        int sliderIndex = 0;

        for (javafx.scene.Node node : slidersContainer.getChildren()) {
            if (node instanceof VBox vBox) {
                for (javafx.scene.Node child : vBox.getChildren()) {
                    if (child instanceof Slider slider) {
                        double initGainValue = listener.getEqualizerBandGain(sliderIndex);
                        slider.setValue(initGainValue);
                        sliderIndex++;
                        break;
                    }
                }
            }
        }
    }

}
