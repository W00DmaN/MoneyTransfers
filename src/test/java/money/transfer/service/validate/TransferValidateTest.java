package money.transfer.service.validate;

import money.transfer.service.exception.ValidateTransferException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TransferValidateTest {

    @Test
    void validateTransferNegativeSumm() {
        assertThrows(ValidateTransferException.class, () -> TransferValidate.validate(1l, 1l, -1l));
    }

    void validateTransferForYourself() {
        assertThrows(ValidateTransferException.class, () -> TransferValidate.validate(1L, 1L, 1L));
    }
}