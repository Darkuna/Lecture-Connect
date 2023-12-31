package at.qe.memori.g7.t1.exceptions.card;

public class DuplicateCardException extends Exception {
    public DuplicateCardException() {
    }

    public DuplicateCardException(String message) {
        super(message);
    }
}
