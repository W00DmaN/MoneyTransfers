package money.transfer.service.validate;

import money.transfer.service.exception.ValidateTransferException;

public final class TransferValidate {
    public static void validate(long fromUserId, long toUserId, long summ) {
        if (summ <= 0) {
            throw new ValidateTransferException("Summ can't be less 1 cent");
        }
        if (fromUserId == toUserId) {
            throw new ValidateTransferException("You can't create transfer for yourself");
        }
    }
}
