package money.transfer.service;

import money.transfer.rest.model.req.TransferRequest;
import money.transfer.rest.model.res.TransferResponse;

public interface TransferService {
    TransferResponse transferMoney(long fromUserId, long toUserId, TransferRequest request);

    void deleteAll();
}
