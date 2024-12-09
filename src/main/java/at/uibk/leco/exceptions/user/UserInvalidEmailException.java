package at.uibk.leco.exceptions.user;

public class UserInvalidEmailException extends Exception{
    public UserInvalidEmailException(String message) {
        super(message);
    }
}
