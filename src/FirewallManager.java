// FirewallManager.java
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;  // ADD THIS IMPORT for BorderLayout

public class FirewallManager {
    private ArrayList<String> keywords;
    private boolean enabled;

    public FirewallManager() {
        keywords = new ArrayList<>();
        enabled = true;
    }

    public boolean isBlocked(String url) {
        if (!enabled) return false;

        for (String keyword : keywords) {
            if (url.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void addKeyword(String keyword) {
        if (!keywords.contains(keyword)) {
            keywords.add(keyword);
            FileHandler.saveFirewallKeywords(keywords);
        }
    }

    public void removeKeyword(String keyword) {
        keywords.remove(keyword);
        FileHandler.saveFirewallKeywords(keywords);
    }

    public ArrayList<String> getKeywords() {
        return new ArrayList<>(keywords);
    }

    public void setEnabled(boolean enabled) {  // Fixed typo: Bootean → boolean
        this.enabled = enabled;
        FileHandler.saveFirewallStatus(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void showFirewallDialog(JFrame parent) {
        JPanel panel = new JPanel(new BorderLayout());

        // Keywords list
        JList<String> keywordsList = new JList<>(keywords.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(keywordsList);
        panel.add(scrollPane, BorderLayout.CENTER);  // Fixed variable name

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(parent, "Enter keyword to block:");
            if (keyword != null && !keyword.trim().isEmpty()) {
                addKeyword(keyword);
                keywordsList.setListData(keywords.toArray(new String[0]));
            }
        });

        removeButton.addActionListener(e -> {
            String selected = keywordsList.getSelectedValue();
            if (selected != null) {
                removeKeyword(selected);
                keywordsList.setListData(keywords.toArray(new String[0]));
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(parent, panel, "Firewall Keywords", JOptionPane.PLAIN_MESSAGE);
    }

    public void loadFirewallData() {
        keywords = FileHandler.loadFirewallKeywords();
        enabled = FileHandler.loadFirewallStatus();
    }
}