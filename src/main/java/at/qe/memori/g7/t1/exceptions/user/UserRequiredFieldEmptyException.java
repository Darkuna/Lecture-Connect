package at.qe.memori.g7.t1.exceptions.user;

public class UserRequiredFieldEmptyException extends Exception {
    public UserRequiredFieldEmptyException(String field) {
        super(field);
    }
}
