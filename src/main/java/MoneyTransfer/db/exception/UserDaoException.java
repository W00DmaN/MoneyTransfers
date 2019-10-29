package MoneyTransfer.db.exception;

public class UserDaoException extends RuntimeException {

    public UserDaoException(String message) {
        super(message);
    }

    public UserDaoException(Throwable cause) {
        super(cause);
    }
}
