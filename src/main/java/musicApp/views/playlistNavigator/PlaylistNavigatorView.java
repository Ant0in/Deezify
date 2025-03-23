package musicApp.views.playlistNavigator;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import musicApp.controllers.PlaylistCellController;
import musicApp.controllers.PlaylistNavigatorController;
import musicApp.models.Library;
import musicApp.utils.LanguageManager;
import musicApp.views.View;

import java.util.List;

public class PlaylistNavigatorView extends View<PlaylistNavigatorView, PlaylistNavigatorController> {

    @FXML
    private ListView<Library> listView;

    @FXML
    private Button createPlaylist;

    public PlaylistNavigatorView() {}

    @Override
    public void init() {
        initListView();
        enableClickToSelect();
        setButtonActions();
        initTranslation();
    }

    private void initListView() {
        listView.setCellFactory(_ -> new PlaylistCell(new PlaylistCellController(viewController)));
    }

    private void setButtonActions() {
        createPlaylist.setOnAction(_ -> openEditPopup());
    }

    private void initTranslation() {
        createPlaylist.setText(LanguageManager.getInstance().get("button.create_playlist"));
    }

    /**
     * TODO: To be refactored to a separate class.
     */
    private void openEditPopup() {
        Stage stage = new Stage();
        stage.setTitle(LanguageManager.getInstance().get("create_playlist.title"));

        TextField nameField = new TextField();

        nameField.setText(LanguageManager.getInstance().get("create_playlist.name"));

        // few buttons
        Button createButton = new Button(LanguageManager.getInstance().get("create_playlist.create"));
        Button cancelButton = new Button(LanguageManager.getInstance().get("create_playlist.cancel"));

        createButton.setOnAction(e -> {
            viewController.createPlaylist(nameField.getText(), null);
            stage.close();
        });

        cancelButton.setOnAction(e -> {
            stage.close();
        });

        VBox popupLayout = new VBox(10,
                new Label(LanguageManager.getInstance().get("create_playlist.name")), nameField,
                createButton, cancelButton);
        Scene popupScene = new Scene(popupLayout, 300, 250);

        stage.setScene(popupScene);
        stage.show();

    }


    public void update(List<Library> libraries) {
        listView.getItems().clear();
        listView.getItems().addAll(libraries);
    }

    /**
     * Enable double click to play.
     */
    public void enableClickToSelect() {
        listView.setOnMouseClicked(_ -> {
            if (listView.getSelectionModel().getSelectedItem() == null) return;
            viewController.setSelectedLibrary(listView.getSelectionModel().getSelectedItem());
        });
    }

}
