package money.transfer.dao.exception;

public class TransferNotFoundException extends TransferException {
    public TransferNotFoundException(String message) {
        super(message);
    }
}
