package MusicApp.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

class PlayingSong {
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);

    public DoubleProperty progressProperty() { return progress; }
    public void setProgress(double value) { progress.set(value); }
}