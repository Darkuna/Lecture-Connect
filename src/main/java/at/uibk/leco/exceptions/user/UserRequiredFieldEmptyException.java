package at.uibk.leco.exceptions.user;

public class UserRequiredFieldEmptyException extends Exception {
    public UserRequiredFieldEmptyException(String field) {
        super(field);
    }
}
