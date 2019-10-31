package MoneyTransfer.db.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private final Long id;
    private final String name;
    private final long cents;

    private final int scaleForMoney = 2;

    public User(String name) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.name = name;
        this.cents = 0L;
    }
}
