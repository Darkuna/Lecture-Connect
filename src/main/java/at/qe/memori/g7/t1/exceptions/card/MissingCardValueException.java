package at.qe.memori.g7.t1.exceptions.card;

public class MissingCardValueException extends Exception {
    public MissingCardValueException() {
        super();
    }

    public MissingCardValueException(String message) {
        super(message);
    }
}
