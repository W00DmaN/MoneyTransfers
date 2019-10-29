package MoneyTransfer.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class User {
    private final Long id;
    private final String name;

    public User(String name) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.name = name;
    }
}
