package MusicApp.Models;

public class Song {
    private String title;
    private String imagePath;

    public Song(String title, String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }
}

