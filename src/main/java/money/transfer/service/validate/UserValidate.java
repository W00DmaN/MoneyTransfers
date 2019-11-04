package money.transfer.service.validate;

import money.transfer.service.exception.ValidateUserException;

public final class UserValidate {
    public static void validateAddMoneySumm(final long summ) {
        if (summ <= 0) {
            throw new ValidateUserException("You can't add summ less 1 cent");
        }
    }
}
