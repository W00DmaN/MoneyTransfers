package MoneyTransfer.db.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private final Long id;
    private final String name;
    private final BigDecimal cents;

    public User(String name) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.name = name;
        this.cents = BigDecimal.ZERO.ZERO.setScale(2);
    }
}
