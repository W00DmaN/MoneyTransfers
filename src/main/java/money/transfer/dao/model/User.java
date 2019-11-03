package money.transfer.dao.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private final Long id;
    private final String name;
    private final long cents;

    private final int scaleForMoney = 2;

    public User(String name) {
        this.id = -1l;
        this.name = name;
        this.cents = 0L;
    }
}
