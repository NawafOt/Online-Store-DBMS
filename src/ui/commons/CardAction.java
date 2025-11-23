package ui.commons;

public class CardAction {
    public final String text;
    public final Runnable action;

    public CardAction(String text, Runnable action) {
        this.text = text;
        this.action = action;
    }
}
