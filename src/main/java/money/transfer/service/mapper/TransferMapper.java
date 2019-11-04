package money.transfer.service.mapper;

import money.transfer.dao.model.Transfer;
import money.transfer.rest.model.res.TransferResponse;

public final class TransferMapper {
    public static TransferResponse adaprToResp(final Transfer transfer) {
        return new TransferResponse(transfer.getId(), transfer.getFromUserId(), transfer.getToUserId(), transfer.getCount());
    }
}
