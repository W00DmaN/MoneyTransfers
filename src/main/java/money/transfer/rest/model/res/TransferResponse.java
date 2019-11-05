package money.transfer.rest.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {
    private long id;
    private long userFrom;
    private long userTo;
    private long summ;
}
