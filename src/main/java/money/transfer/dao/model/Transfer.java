package money.transfer.dao.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Transfer {
    private long id;
    private long fromUserId;
    private long toUserId;
    private long count;
}
