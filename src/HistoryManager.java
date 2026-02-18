// HistoryManager.java
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryManager {
    private ArrayList<String> history;
    private SimpleDateFormat dateFormat;

    public HistoryManager() {
        history = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd/MM/yy, HH:mm");
    }

    public void addToHistory(String url) {
        String timestamp = dateFormat.format(new Date());
        String entry = url + " - " + timestamp;
        history.add(entry);
        FileHandler.saveHistory(history);
    }

    public ArrayList<String> getHistory() {
        return new ArrayList<>(history);
    }

    public void clearHistory() {
        history.clear();
        FileHandler.saveHistory(history);
    }

    public void loadHistory() {
        history = FileHandler.loadHistory();
    }
}