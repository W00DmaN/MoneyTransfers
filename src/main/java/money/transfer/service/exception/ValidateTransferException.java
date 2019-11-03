package money.transfer.service.exception;

public class ValidateTransferException extends RuntimeException {
    public ValidateTransferException(String message) {
        super(message);
    }
}
