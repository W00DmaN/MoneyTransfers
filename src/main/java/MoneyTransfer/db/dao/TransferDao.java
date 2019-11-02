package MoneyTransfer.db.dao;

import MoneyTransfer.db.model.Transfer;

import java.util.List;

public interface TransferDao {
    Transfer create(long fromUserId, long toUserId, long count);

    Transfer getById(long transferId);

    List<Transfer> getAllTransfers();

    void deleteAll();
}
