// Favorite.java
public class Favorite {
    private String title;
    private String url;

    public Favorite(String title, String url) {
        this.title = title;
        this.url = url;
    }

    // Getters
    public String getTitle() { return title; }
    public String getUrl() { return url; }

    @Override
    public String toString() {
        return title + " - " + url;
    }
}