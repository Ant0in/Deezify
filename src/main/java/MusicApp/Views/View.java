package MusicApp.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import MusicApp.Controllers.ViewController;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public abstract class View {
    protected Scene scene;
    protected ViewController viewController;
    protected Pane rootPane;

    public View(){
    }

    public void setController(ViewController viewController){
        this.viewController = viewController;
    }

    /**
     * Initialize the FXML scene.
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public void initializeScene(String fxmlPath) throws IOException {
        URL url = View.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        rootPane = loader.load();
        this.scene = new Scene(rootPane);
    }

    public Pane getRoot(){
        return rootPane;
    }

    public void initialize(){
    }

}
