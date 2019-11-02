package MoneyTransfer.service.mapper;

import MoneyTransfer.db.model.Transfer;
import MoneyTransfer.rest.model.res.TransferResponse;

public final class TransferMapper {
    public static TransferResponse adaprToResp(Transfer transfer) {
        return new TransferResponse(transfer.getId(), transfer.getFromUserId(), transfer.getToUserId(), transfer.getCount());
    }
}
