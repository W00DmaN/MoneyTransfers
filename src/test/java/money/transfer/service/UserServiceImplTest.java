package money.transfer.service;

import io.micronaut.test.annotation.MicronautTest;
import money.transfer.dao.exception.UserNotFoundException;
import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class UserServiceImplTest {

    @Inject
    private UserService userService;

    @AfterEach
    void after() {
        userService.deleteAll();
    }

    @Test
    void createUser() {
        String name = "Tim_Test";
        UserResponse result = generateUser(name);
        assertEquals(name, result.getName());
        assertTrue(result.getId() > 0);
    }

    @Test
    void getAll() {
        String name1 = "Tim1_Test";
        String name2 = "Tim2_Test";
        UserResponse user1 = generateUser(name1);
        UserResponse user2 = generateUser(name2);
        assertNotEquals(user1.getId(), user2.getId());
        assertTrue(user1.getId() < user2.getId());

        List<UserResponse> result = userService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    void getById() {
        String name1 = "Tim1_Test";
        String name2 = "Tim2_Test";
        UserResponse user1 = generateUser(name1);
        UserResponse user2 = generateUser(name2);

        assertEquals(user1, userService.getById(user1.getId()));
        assertEquals(user2, userService.getById(user2.getId()));
    }

    @Test
    void deleteById() {
        String name = "Tim1_Test";
        UserResponse user = generateUser(name);

        userService.deleteById(user.getId());

        assertThrows(UserNotFoundException.class, () -> userService.getById(user.getId()));
    }

    @Test
    void deleteAll() {
        String name1 = "Tim1_Test";
        String name2 = "Tim2_Test";
        generateUser(name1);
        generateUser(name2);

        userService.deleteAll();

        assertTrue(userService.getAll().isEmpty());
    }

    @Test
    void addMoney() {
        String name = "Tim1_Test";
        long addedMoney = 100L;
        UserResponse user = generateUser(name);
        DepositUserRequest depositUserRequest = new DepositUserRequest(addedMoney);
        UserResponse result = userService.addMoney(user.getId(), depositUserRequest);

        assertEquals(addedMoney, result.getCents() - user.getCents());
    }

    @Test
    void parallelAddMoney() {
        int countAddMoney = 100;

        String name = "Tim1_Test";
        long addedMoney = 1;
        UserResponse user = generateUser(name);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Thread thread = new Thread(() -> {
            DepositUserRequest depositUserRequest = new DepositUserRequest(addedMoney);
            userService.addMoney(user.getId(), depositUserRequest);
        });

        List<Future> list = new ArrayList<>();

        for (int i = 0; i < countAddMoney; i++) {
            list.add(executorService.submit(thread));
        }

        list.parallelStream().forEach(x -> {
            try {
                x.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        assertEquals(addedMoney * countAddMoney, userService.getById(user.getId()).getCents());
    }

    private UserResponse generateUser(String name) {
        CreateUserRequest request = new CreateUserRequest(name);
        return userService.createUser(request);
    }
}