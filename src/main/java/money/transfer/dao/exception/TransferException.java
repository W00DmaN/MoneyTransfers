package money.transfer.dao.exception;

public class TransferException extends MoneyTransferDaoException {

    public TransferException(String message) {
        super(message);
    }

    public TransferException(Throwable cause) {
        super(cause);
    }
}
