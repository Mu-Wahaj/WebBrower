// FavoritesManager.java
import java.util.ArrayList;
import javax.swing.*;

public class FavoritesManager {
    private ArrayList<Favorite> favorites;

    public FavoritesManager() {
        favorites = new ArrayList<>();
    }

    public void addFavorite(String title, String url) {
        favorites.add(new Favorite(title, url));
        FileHandler.saveFavorites(favorites);
    }

    public ArrayList<Favorite> getFavorites() {
        return new ArrayList<>(favorites);
    }

    public void removeFavorite(int index) {
        if (index >= 0 && index < favorites.size()) {
            favorites.remove(index);
            FileHandler.saveFavorites(favorites);
        }
    }

    public void showAddFavoriteDialog(String currentUrl, JFrame parent) {
        String title = JOptionPane.showInputDialog(parent, "Enter title for favorite:", "Add Favorite", JOptionPane.QUESTION_MESSAGE);
        if (title != null && !title.trim().isEmpty()) {
            addFavorite(title, currentUrl);
            JOptionPane.showMessageDialog(parent, "Favorite added successfully!");
        }
    }

    public void loadFavorites() {
        favorites = FileHandler.loadFavorites();
    }
}