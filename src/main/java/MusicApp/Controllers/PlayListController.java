package MusicApp.Controllers;


import MusicApp.Models.Song;
import MusicApp.Views.PlayListView;
import javafx.beans.binding.BooleanBinding;

import java.util.List;

public class PlayListController extends ViewController<PlayListView, PlayListController> {
    private final PlayerController playerController;

    public PlayListController(PlayerController controller) {
        super(new PlayListView());
        this.playerController = controller;
        initView("/fxml/PlayList.fxml");
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public BooleanBinding isSelected() {
        return view.isSelected();
    }

    public void clearSelection() {
        view.clearSelection();
    }

    public int getSelectedIndex() {
        return view.getSelectedSongIndex();
    }

    public List<Song> searchLibrary(String query) {
        return playerController.searchLibrary(query);
    }

    public void updatePlayListView() {
        view.updatePlayListView();
    }
}
