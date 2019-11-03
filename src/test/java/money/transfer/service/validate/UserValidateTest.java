package money.transfer.service.validate;

import money.transfer.service.exception.ValidateUserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidateTest {

    @Test
    void testCheckNegativeSummInValidateAddMoneySumm() {
        assertThrows(ValidateUserException.class, () -> UserValidate.validateAddMoneySumm(-1L));
    }
}