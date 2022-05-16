package GOGame;

import java.io.IOException;

public interface GameWindow {
    void start(Engine engine, String assetsPath) throws IOException;
    void sleep(Integer amount);
    String requestChoice(String text, String[] choices);
    void onLogUpdate(String message);
}
