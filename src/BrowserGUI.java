// BrowserGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class BrowserGUI {
    private JFrame frame;
    private JEditorPane sitePanel;
    private JTextField addressBar;
    private JButton backButton, forwardButton, homeButton, refreshButton, goButton;
    private JButton historyButton, favoritesButton, searchButton;
    private JMenuBar menuBar;

    private WebBrowser browser;

    public BrowserGUI(WebBrowser browser) {
        this.browser = browser;
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Java Web Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        createMenuBar();
        createToolbar();
        createSitePanel();

        frame.setVisible(true);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem setHomePageItem = new JMenuItem("Set Home Page");
        JMenuItem exitItem = new JMenuItem("Exit");

        setHomePageItem.addActionListener(e -> browser.setHomePage());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(setHomePageItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Firewall menu
        JMenu firewallMenu = new JMenu("Firewall");
        JMenuItem manageKeywordsItem = new JMenuItem("Add/Delete Key Word");
        JMenuItem toggleFirewallItem = new JMenuItem("Enable/Disable");

        manageKeywordsItem.addActionListener(e -> browser.manageFirewallKeywords());
        toggleFirewallItem.addActionListener(e -> browser.toggleFirewall());

        firewallMenu.add(manageKeywordsItem);
        firewallMenu.add(toggleFirewallItem);

        menuBar.add(fileMenu);
        menuBar.add(firewallMenu);

        frame.setJMenuBar(menuBar);
    }

    private void createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create buttons (you'll need to add actual image files)
        backButton = createButton("Back", "back.png");
        forwardButton = createButton("Forward", "forward.png");
        homeButton = createButton("Home", "home.png");
        refreshButton = createButton("Refresh", "refresh.png");
        historyButton = createButton("History", "history.png");
        favoritesButton = createButton("Favorites", "favorites.png");
        searchButton = createButton("Search", "search.png");

        addressBar = new JTextField(30);
        goButton = new JButton("Go");

        // Add action listeners
        backButton.addActionListener(e -> browser.goBack());
        forwardButton.addActionListener(e -> browser.goForward());
        homeButton.addActionListener(e -> browser.goHome());
        refreshButton.addActionListener(e -> browser.refresh());
        historyButton.addActionListener(e -> browser.showHistory());
        favoritesButton.addActionListener(e -> browser.showFavorites());
        searchButton.addActionListener(e -> browser.searchPage());
        goButton.addActionListener(e -> browser.navigateTo(addressBar.getText()));

        addressBar.addActionListener(e -> browser.navigateTo(addressBar.getText()));

        // Add components to toolbar
        toolbar.add(backButton);
        toolbar.add(forwardButton);
        toolbar.add(homeButton);
        toolbar.add(refreshButton);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(historyButton);
        toolbar.add(favoritesButton);
        toolbar.add(searchButton);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(new JLabel("Address:"));
        toolbar.add(addressBar);
        toolbar.add(goButton);

        frame.add(toolbar, BorderLayout.NORTH);
    }

    private void createSitePanel() {
        sitePanel = new JEditorPane();
        sitePanel.setContentType("text/html");
        sitePanel.setEditable(false);

        // Handle hyperlink clicks
        sitePanel.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    browser.navigateTo(e.getURL().toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(sitePanel);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        // You can add icon loading here:
        // try {
        //     ImageIcon icon = new ImageIcon(iconPath);
        //     button.setIcon(icon);
        // } catch (Exception e) {
        //     System.err.println("Could not load icon: " + iconPath);
        // }
        return button;
    }

    // Getters for components
    public JEditorPane getSitePanel() { return sitePanel; }
    public JTextField getAddressBar() { return addressBar; }
    public JButton getBackButton() { return backButton; }
    public JButton getForwardButton() { return forwardButton; }
    public JFrame getFrame() { return frame; }
}