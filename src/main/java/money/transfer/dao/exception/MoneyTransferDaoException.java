package money.transfer.dao.exception;

public class MoneyTransferDaoException extends RuntimeException {

    public MoneyTransferDaoException(String message) {
        super(message);
    }

    public MoneyTransferDaoException(Throwable cause) {
        super(cause);
    }
}
