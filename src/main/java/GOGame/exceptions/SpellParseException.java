package GOGame.exceptions;

public class SpellParseException extends SpellException {
    public SpellParseException(String spellLine, String reason) {
        super(String.format("can't parse spell line %s: %s", spellLine, reason));
    }
}
