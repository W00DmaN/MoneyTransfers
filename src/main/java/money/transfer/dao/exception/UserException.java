package money.transfer.dao.exception;

public class UserException extends MoneyTransferDaoException {
    public UserException(String message) {
        super(message);
    }

    public UserException(Throwable cause) {
        super(cause);
    }
}
