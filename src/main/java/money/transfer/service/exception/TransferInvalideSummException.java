package money.transfer.service.exception;

public class TransferInvalideSummException extends RuntimeException {
    public TransferInvalideSummException(String message) {
        super(message);
    }
}
