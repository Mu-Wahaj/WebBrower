// FileHandler.java
import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private static final String HOME_PAGE_FILE = "homepage.txt";
    private static final String HISTORY_FILE = "history.txt";
    private static final String FAVORITES_FILE = "favorites.txt";
    private static final String FIREWALL_FILE = "firewall.txt";
    private static final String FIREWALL_STATUS_FILE = "firewall_status.txt";

    // Home page methods
    public static void saveHomePage(String url) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HOME_PAGE_FILE))) {
            writer.println(url);
        } catch (IOException e) {
            System.err.println("Error saving home page: " + e.getMessage());
        }
    }

    public static String loadHomePage() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HOME_PAGE_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            return "http://text.npr.org" ; // ← SIMPLER PAGE THAT WORKS
        }
    }

    // History methods
    public static void saveHistory(ArrayList<String> history) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE))) {
            for (String entry : history) {
                writer.println(entry);
            }
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }

    public static ArrayList<String> loadHistory() {
        ArrayList<String> history = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            // Return empty history if file doesn't exist
        }
        return history;
    }

    // Favorites methods
    public static void saveFavorites(ArrayList<Favorite> favorites) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FAVORITES_FILE))) {
            for (Favorite fav : favorites) {
                writer.println(fav.getTitle() + "|||" + fav.getUrl());
            }
        } catch (IOException e) {
            System.err.println("Error saving favorites: " + e.getMessage());
        }
    }

    public static ArrayList<Favorite> loadFavorites() {
        ArrayList<Favorite> favorites = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FAVORITES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|\\|\\|");
                if (parts.length == 2) {
                    favorites.add(new Favorite(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            // Return empty favorites if file doesn't exist
        }
        return favorites;
    }

    // Firewall methods
    public static void saveFirewallKeywords(ArrayList<String> keywords) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FIREWALL_FILE))) {
            for (String keyword : keywords) {
                writer.println(keyword);
            }
        } catch (IOException e) {
            System.err.println("Error saving firewall keywords: " + e.getMessage());
        }
    }

    public static ArrayList<String> loadFirewallKeywords() {
        ArrayList<String> keywords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FIREWALL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keywords.add(line);
            }
        } catch (IOException e) {
            // Return empty list if file doesn't exist
        }
        return keywords;
    }

    public static void saveFirewallStatus(boolean enabled) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FIREWALL_STATUS_FILE))) {
            writer.println(enabled);
        } catch (IOException e) {
            System.err.println("Error saving firewall status: " + e.getMessage());
        }
    }

    public static boolean loadFirewallStatus() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FIREWALL_STATUS_FILE))) {
            return Boolean.parseBoolean(reader.readLine());
        } catch (IOException e) {
            return true; // Default enabled
        }
    }
}