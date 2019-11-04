package money.transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
import money.transfer.rest.model.req.TransferRequest;
import money.transfer.rest.model.res.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class TransferServiceImplTest {

    @Inject
    TransferService transferService;
    @Inject
    UserService userService;

    @AfterEach
    void after() {
        transferService.deleteAll();
        userService.deleteAll();
    }

    @Test
    void transferMoney() {
        long money = 100L;
        UserResponse user1 = generateUser("Tim_Test1", money);
        UserResponse user2 = generateUser("Tim_Test2", money);

        TransferRequest transferRequest = new TransferRequest(money);
        transferService.transferMoney(user1.getId(), user2.getId(), transferRequest);

        user1 = userService.getById(user1.getId());
        user2 = userService.getById(user2.getId());

        assertEquals(0L, user1.getCents());
        assertEquals(money * 2, user2.getCents());
    }

    @Test
    void testConsistentParallelTransferMoney() {
        long money = 1000L;
        long transferMoney = 1L;
        int countCallTransfer = 100;

        UserResponse user1 = generateUser("Tim_Test1", money);
        UserResponse user2 = generateUser("Tim_Test2", money);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Thread thread1 = new Thread(() -> {
            TransferRequest transferRequest = new TransferRequest(transferMoney);
            transferService.transferMoney(user1.getId(), user2.getId(), transferRequest);
        });
        Thread thread2 = new Thread(() -> {
            TransferRequest transferRequest = new TransferRequest(transferMoney);
            transferService.transferMoney(user2.getId(), user1.getId(), transferRequest);
        });

        List<Future> list = new ArrayList<>();

        for (int i = 0; i < countCallTransfer; i++) {
            list.add(executorService.submit(thread1));
            list.add(executorService.submit(thread2));
        }

        list.parallelStream().forEach(x -> {
            try {
                x.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        long moneyUser1 = userService.getById(user1.getId()).getCents();
        long moneyUser2 = userService.getById(user2.getId()).getCents();

        assertEquals(money * 2, moneyUser1 + moneyUser2);
    }

    @Test
    void deleteAll() {
    }

    private UserResponse generateUser(String name, long money) {
        CreateUserRequest request = new CreateUserRequest(name);
        UserResponse user = userService.createUser(request);
        DepositUserRequest depositUserRequest = new DepositUserRequest(money);
        return userService.addMoney(user.getId(), depositUserRequest);
    }
}