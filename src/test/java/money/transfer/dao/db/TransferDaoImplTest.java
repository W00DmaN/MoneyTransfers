package money.transfer.dao.db;

import io.micronaut.test.annotation.MicronautTest;
import money.transfer.dao.TransferDao;
import money.transfer.dao.UserDao;
import money.transfer.dao.exception.TransferException;
import money.transfer.dao.exception.TransferNotFoundException;
import money.transfer.dao.model.Transfer;
import money.transfer.dao.model.User;
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
    void createTransferForNotExistFromUser() {
        assertThrows(TransferException.class, () -> transferDao.create(1L, 1L, 1L));
    }

    @Test
    void positiveCreateTransferWithExistUsers() {
        User fromUser = generateUser("FromUser_Test");
        User toUser = generateUser("ToUser_Test");
        long summ = 100L;
        Transfer transfer = transferDao.create(fromUser.getId(), toUser.getId(), summ);
        assertEquals(fromUser.getId(), transfer.getFromUserId());
        assertEquals(toUser.getId(), transfer.getToUserId());
        assertEquals(summ, transfer.getCount());
    }

    @Test
    void positiveGetTransferByTransferId() {
        User fromUser = generateUser("FromUser_Test");
        User toUser = generateUser("ToUser_Test");
        long summ = 100L;
        Transfer transfer = transferDao.create(fromUser.getId(), toUser.getId(), summ);
        Transfer result = transferDao.getById(transfer.getId());
        assertEquals(transfer, result);
    }

    @Test
    void getTransferByTransferIdForNotExistTransferId() {
        assertThrows(TransferNotFoundException.class, () -> transferDao.getById(-1L));
    }

    @Test
    void positiveGetAllTransfers() {
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

    @Test
    void getAllTransferWithoutTransferInSystem() {
        List<Transfer> results = transferDao.getAllTransfers();
        assertTrue(results.isEmpty());
    }

    private User generateUser(String name) {
        User user = new User(name);
        return userDao.createUser(user);
    }
}