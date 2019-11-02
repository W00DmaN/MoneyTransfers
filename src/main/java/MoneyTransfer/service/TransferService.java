package MoneyTransfer.service;

import MoneyTransfer.rest.model.req.TransferRequest;
import MoneyTransfer.rest.model.res.TransferResponse;

public interface TransferService {
    TransferResponse transferMoney(long fromUserId, long toUserId, TransferRequest request);

    void deleteAll();
}
