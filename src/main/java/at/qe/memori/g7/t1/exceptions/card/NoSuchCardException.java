package at.qe.memori.g7.t1.exceptions.card;

public class NoSuchCardException extends Exception {
    public NoSuchCardException() {
    }

    public NoSuchCardException(String message) {
        super(message);
    }
}
