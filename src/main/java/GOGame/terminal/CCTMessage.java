package GOGame.terminal;

import GOGame.Utility;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CCTMessage implements DrawableAsLine{
    static final String colorMatchRegex = "\\$\\{([\\w|-]+)\\}([^\\$]*)";
    static final Pattern colorPattern = Pattern.compile(colorMatchRegex);

    private List<String> messages;
    private List<TextColor> fgColors;
    private List<TextColor> bgColors;

    public CCTMessage(String message) {
//        if message.startsWith()
        Matcher matcher;
        if (message.startsWith("$")) {
            matcher = colorPattern.matcher(message);
        }
        else {
            matcher = colorPattern.matcher("${normal}" + message);
        }
        this.messages = new ArrayList<>();
        this.fgColors = new ArrayList<>();
        this.bgColors = new ArrayList<>();
        while (matcher.find()) {
            var color = matcher.group(1);
            var pair = TerminalUtility.parseColor(color);
            var fg = pair.getFirst();
            var bg = pair.getSecond();
            this.fgColors.add(fg);
            this.bgColors.add(bg);
            var line = matcher.group(2);
            this.messages.add(line);
        }
    }

    public int length() {
        var result = 0;
        for (var m : messages) {
            result += m.length();
        }
        return result;
    }

    public void draw(Terminal t, int x, int y) {
        this.drawLine(t, x, y, false);
    }

    public int foo() {
        return fgColors.get(0).getGreen();
    }

    @Override
    public void drawLine(Terminal t, int x, int y, boolean reverseColor) {
        int offset = 0;
        try {
            for (int i = 0; i < this.messages.size(); i++) {
                if (reverseColor) {
                    t.setForegroundColor(this.bgColors.get(i));
                    t.setBackgroundColor(this.fgColors.get(i));
                }
                else {
                    t.setForegroundColor(this.fgColors.get(i));
                    t.setBackgroundColor(this.bgColors.get(i));
                }
                var m = this.messages.get(i);
                TerminalUtility.putAt(t, x + offset, y, m);
                TerminalUtility.resetColors(t);
                offset += m.length();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
