package at.qe.memori.g7.t1.exceptions.user;

public class UserInvalidEmailException extends Exception{
    public UserInvalidEmailException(String message) {
        super(message);
    }
}
