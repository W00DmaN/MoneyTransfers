package money.transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
import money.transfer.rest.model.req.TransferRequest;
import money.transfer.rest.model.res.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class TransferServiceImplTest {

    @Inject
    TransferService transferService;
    @Inject
    UserService userService;

    @AfterEach
    void after(){
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
    void deleteAll() {
    }

    private UserResponse generateUser(String name, long money) {
        CreateUserRequest request = new CreateUserRequest(name);
        UserResponse user = userService.createUser(request);
        DepositUserRequest depositUserRequest = new DepositUserRequest(money);
        return userService.addMoney(user.getId(), depositUserRequest);
    }
}