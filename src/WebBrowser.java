// WebBrowser.java
import javax.swing.*;
import java.awt.*;           // For BorderLayout
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker;  // ← ADD THIS IMPORT


public class WebBrowser {
    private BrowserGUI gui;
    private NavigationManager navigationManager;
    private HistoryManager historyManager;
    private FavoritesManager favoritesManager;
    private FirewallManager firewallManager;

    private String currentURL;
    private String homePage;

    public WebBrowser() {
        // Initialize managers
        navigationManager = new NavigationManager();
        historyManager = new HistoryManager();
        favoritesManager = new FavoritesManager();
        firewallManager = new FirewallManager();

        // Load persistent data
        loadPersistentData();

        // Create GUI
        gui = new BrowserGUI(this);

        // Navigate to home page
        navigateTo(homePage);
    }

    private void loadPersistentData() {
        homePage = FileHandler.loadHomePage();
        historyManager.loadHistory();
        favoritesManager.loadFavorites();
        firewallManager.loadFirewallData();
    }

    public void navigateTo(String url) {
        if (url == null || url.trim().isEmpty()) return;

        // Add http:// if missing
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        // Check firewall
        if (firewallManager.isBlocked(url)) {
            showMessage("Access Blocked", "This site is blocked by firewall.");
            return;
        }

        try {
            // Clear previous content first
            gui.getSitePanel().setText("<html><body><h3>Loading...</h3></body></html>");

            // Load in a simple way
            try {
                gui.getSitePanel().setPage(url);
            } catch (Exception e) {
                // If page fails to load, show simple HTML
                String errorHtml = "<html><body style='padding:20px;'>" +
                        "<h2>⚠️ Page Load Error</h2>" +
                        "<p><b>URL:</b> " + url + "</p>" +
                        "<p><b>Error:</b> " + e.getMessage() + "</p>" +
                        "<p><b>Tip:</b> Try loading a simpler page like:</p>" +
                        "<ul>" +
                        "<li><a href='http://example.com'>http://example.com</a></li>" +
                        "<li><a href='http://info.cern.ch'>http://info.cern.ch</a></li>" +
                        "<li><a href='http://w3.org'>http://w3.org</a></li>" +
                        "</ul>" +
                        "</body></html>";
                gui.getSitePanel().setText(errorHtml);
            }

            currentURL = url;
            gui.getAddressBar().setText(url);

            // Update navigation
            navigationManager.navigateTo(url);
            updateNavigationButtons();

            // Add to history
            historyManager.addToHistory(url);

        } catch (Exception e) {
            showMessage("Navigation Error", "Invalid URL: " + url);
        }
    }

    public void goBack() {
        if (navigationManager.canGoBack()) {
            String previousUrl = navigationManager.goBack(currentURL);
            navigateToDirect(previousUrl);
        }
    }

    public void goForward() {
        if (navigationManager.canGoForward()) {
            String nextUrl = navigationManager.goForward(currentURL);
            navigateToDirect(nextUrl);
        }
    }

    private void navigateToDirect(String url) {
        try {
            gui.getSitePanel().setPage(url);
            currentURL = url;
            gui.getAddressBar().setText(url);
            updateNavigationButtons();
        } catch (IOException e) {
            showMessage("Navigation Error", "Could not load: " + url);
        }
    }

    public void goHome() {
        navigateTo(homePage);
    }

    public void refresh() {
        navigateTo(currentURL);
    }

    public void setHomePage() {
        String newHomePage = JOptionPane.showInputDialog(gui.getFrame(), "Enter new home page URL:", homePage);
        if (newHomePage != null && !newHomePage.trim().isEmpty()) {
            homePage = newHomePage;
            FileHandler.saveHomePage(homePage);
            showMessage("Success", "Home page updated to: " + homePage);
        }
    }

    public void showHistory() {
        ArrayList<String> history = historyManager.getHistory();
        JList<String> historyList = new JList<>(history.toArray(new String[0]));

        historyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = historyList.getSelectedValue();
                if (selected != null) {
                    // Extract URL from history entry (format: "url - timestamp")
                    String url = selected.split(" - ")[0];
                    navigateTo(url);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(historyList);
        JButton closeButton = new JButton("Close");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("History (Last 3 days):"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(gui.getFrame(), "Browser History", false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(panel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(gui.getFrame());

        closeButton.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    public void showFavorites() {
        ArrayList<Favorite> favorites = favoritesManager.getFavorites();
        JList<Favorite> favoritesList = new JList<>(favorites.toArray(new Favorite[0]));

        favoritesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Favorite selected = favoritesList.getSelectedValue();
                if (selected != null) {
                    navigateTo(selected.getUrl());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(favoritesList);
        JButton addButton = new JButton("Add Current");
        JButton removeButton = new JButton("Remove Selected");
        JButton closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(closeButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Favorites:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(gui.getFrame(), "Favorites", false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(panel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(gui.getFrame());

        addButton.addActionListener(e -> favoritesManager.showAddFavoriteDialog(currentURL, gui.getFrame()));
        removeButton.addActionListener(e -> {
            int selectedIndex = favoritesList.getSelectedIndex();
            if (selectedIndex >= 0) {
                favoritesManager.removeFavorite(selectedIndex);
                favoritesList.setListData(favoritesManager.getFavorites().toArray(new Favorite[0]));
            }
        });
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    public void searchPage() {
        String searchTerm = JOptionPane.showInputDialog(gui.getFrame(), "Enter search term:");
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            try {
                String pageText = gui.getSitePanel().getText().toLowerCase();
                int count = countOccurrences(pageText, searchTerm.toLowerCase());
                showMessage("Search Results", "Found " + count + " occurrences of '" + searchTerm + "'");
            } catch (Exception e) {
                showMessage("Search Error", "Could not search current page");
            }
        }
    }

    private int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }

    public void manageFirewallKeywords() {
        firewallManager.showFirewallDialog(gui.getFrame());
    }

    public void toggleFirewall() {
        boolean currentStatus = firewallManager.isEnabled();
        firewallManager.setEnabled(!currentStatus);
        String status = (!currentStatus) ? "enabled" : "disabled";
        showMessage("Firewall", "Firewall " + status);
    }

    private void updateNavigationButtons() {
        gui.getBackButton().setEnabled(navigationManager.canGoBack());
        gui.getForwardButton().setEnabled(navigationManager.canGoForward());
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(gui.getFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WebBrowser();
        });
    }
}