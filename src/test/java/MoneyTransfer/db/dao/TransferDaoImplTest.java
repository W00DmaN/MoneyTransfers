package MoneyTransfer.db.dao;

import MoneyTransfer.db.exception.MoneyTransferDaoException;
import MoneyTransfer.db.model.Transfer;
import MoneyTransfer.db.model.User;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class TransferDaoImplTest {

    @Inject
    private TransferDao transferDao;
    @Inject
    private UserDao userDao;

    @AfterEach
    void after() {
        transferDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    void testCreateTransferForNotExistUsers() {
        assertThrows(MoneyTransferDaoException.class, () -> transferDao.create(1l, 1l, 1l));
    }

    @Test
    void testCreateTranferWithExustUsers() {
        User fromUser = generateUser("FromUser_Test");
        User toUser = generateUser("ToUser_Test");
        long summ = 100L;
        Transfer transfer = transferDao.create(fromUser.getId(), toUser.getId(), summ);
        assertEquals(fromUser.getId(), transfer.getFromUserId());
        assertEquals(toUser.getId(), transfer.getToUserId());
        assertEquals(summ, transfer.getCount());
    }

    @Test
    void testGetTransferByTransferId() {
        User fromUser = generateUser("FromUser_Test");
        User toUser = generateUser("ToUser_Test");
        long summ = 100L;
        Transfer transfer = transferDao.create(fromUser.getId(), toUser.getId(), summ);
        Transfer result = transferDao.getById(transfer.getId());
        assertEquals(transfer, result);
    }

    @Test
    void testGetAllTransfers() {
        User fromUser = generateUser("FromUser_Test");
        User toUser = generateUser("ToUser_Test");
        long summ = 100L;
        Transfer transfer1 = transferDao.create(fromUser.getId(), toUser.getId(), summ);
        Transfer transfer2 = transferDao.create(fromUser.getId(), toUser.getId(), summ);
        List<Transfer> results = transferDao.getAllTransfers();

        assertEquals(2, results.size());
        assertTrue(results.contains(transfer1));
        assertTrue(results.contains(transfer2));
    }

    private User generateUser(String name) {
        User user = new User(name);
        return userDao.createUser(user);
    }
}