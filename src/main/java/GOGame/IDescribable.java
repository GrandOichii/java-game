package GOGame;

import java.util.List;

public interface IDescribable {
    String getDisplayName();
    String getBigDescription(int amount);
    List<String> getAllowedActions();
}
