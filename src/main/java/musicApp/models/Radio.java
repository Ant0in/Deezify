package musicApp.models;

public class Radio {

    private String webUrl;

    public Radio(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }
}