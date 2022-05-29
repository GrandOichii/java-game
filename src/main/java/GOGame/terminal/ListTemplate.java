package GOGame.terminal;

import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;

public class ListTemplate {
    private ArrayList<IDrawableAsLine> items;
    private int maxDisplayAmount;
    private int cursor;
    private int choice;

    public int getChoice() {
        return choice;
    }

    public String getSelected() {
        return items.get(choice).toString();
    }

    private int page;

    public ListTemplate(ArrayList<IDrawableAsLine> items, int maxDisplayAmount) {
        this.items = items;
        this.maxDisplayAmount = maxDisplayAmount;
        this.choice = 0;
        this.cursor = 0;
        this.page = 0;
    }

    public void draw(Terminal terminal, int x, int y, boolean focusSelected) {
        var top = Math.min(maxDisplayAmount, items.size());
//        System.out.println(top);
        for ( int i = 0; i < top; i++) {
            items.get(i + page).drawLine(terminal, x, y + i, (i + page == choice) && focusSelected);
        }
    }

    public void addItem(IDrawableAsLine item) {
        items.add(item);
    }

    public void moveUp() {
        choice--;
        cursor--;
        var size = items.size();
        if (cursor < 0) {
            if (size > maxDisplayAmount) {
                if (page == 0) {
                    cursor = maxDisplayAmount - 1;
                    choice = size - 1;
                    page = size - maxDisplayAmount;
                } else {
                    page--;
                    cursor++;
                }
            } else {
                cursor = size - 1;
                choice = cursor;
            }
        }
    }

    public void moveDown() {
        choice++;
        cursor++;
        var size = items.size();
        if (size > maxDisplayAmount) {
            if (cursor >= maxDisplayAmount) {
                cursor--;
                page++;
                if (choice == size) {
                    choice = 0;
                    cursor = 0;
                    page = 0;
                }
            }
        } else {
            if (cursor >= size) {
                cursor = 0;
                choice = 0;
            }
        }
    }
}
