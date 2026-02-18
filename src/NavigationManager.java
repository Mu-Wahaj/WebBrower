// NavigationManager.java
import java.util.Stack;

public class NavigationManager {
    private Stack<String> backStack;
    private Stack<String> forwardStack;

    public NavigationManager() {
        backStack = new Stack<>();
        forwardStack = new Stack<>();
    }

    public void navigateTo(String url) {
        backStack.push(url);
        forwardStack.clear();
    }

    public String goBack(String currentUrl) {
        if (!backStack.isEmpty()) {
            forwardStack.push(currentUrl);
            return backStack.pop();
        }
        return currentUrl;
    }

    public String goForward(String currentUrl) {
        if (!forwardStack.isEmpty()) {
            backStack.push(currentUrl);
            return forwardStack.pop();
        }
        return currentUrl;
    }

    public boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    public void clearForward() {
        forwardStack.clear();
    }
}