package money.transfer.rest.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import money.transfer.RestClient;
import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
import money.transfer.rest.model.res.UserResponse;
import money.transfer.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class UserControllerTest {

    @Inject
    private RestClient restClient;
    @Inject
    private UserService userService;

    @AfterEach
    void after() {
        userService.deleteAll();
    }

    @Test
    void positiveCreateUser() {
        String userName = "Tim _test";
        UserResponse user = createUser(userName);
        assertEquals(userName, user.getName());
    }

    @Test
    void positiveGetAllUsers() {
        String userName = "Tim_test";
        String userName2 = "Tim2_test";
        UserResponse user1 = createUser(userName);
        UserResponse user2 = createUser(userName2);

        List<UserResponse> result = restClient.getAllUsers().blockingGet();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    void getAllUserWhenSystemWithoutUsers() {
        List<UserResponse> responses = restClient.getAllUsers().blockingGet();
        assertEquals(0, responses.size());
    }

    @Test
    void positiveGetUserById() {
        String userName = "Tim_test";
        UserResponse user = createUser(userName);
        UserResponse result = restClient.getUserById(user.getId()).blockingGet();
        assertEquals(user, result);
    }

    @Test
    void correctlyHandleExceptionGetUserByNonexistentId() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.getUserById(-1L).blockingGet());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void positiveAddMoney() {
        String userName = "Tim_test";
        UserResponse user = createUser(userName);
        assertEquals(0L, user.getCents());

        long depositSumm = 100L;
        DepositUserRequest depositUserRequest = new DepositUserRequest(depositSumm);
        restClient.addMoney(user.getId(), depositUserRequest).blockingGet();

        assertEquals(depositSumm, restClient.getUserById(user.getId()).blockingGet().getCents());

        restClient.addMoney(user.getId(), depositUserRequest).blockingGet();

        assertEquals(depositSumm * 2, restClient.getUserById(user.getId()).blockingGet().getCents());
    }

    @Test
    void correctlyHandleExceptionAddMoneyNonexistentUser() {
        DepositUserRequest depositUserRequest = new DepositUserRequest(100L);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.addMoney(-1L, depositUserRequest).blockingGet());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void correctlyHandleExceptionAddMoneyWithNegativeSumm() {
        DepositUserRequest depositUserRequest = new DepositUserRequest(-1L);
        UserResponse user = createUser("Tim_Test");
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.addMoney(user.getId(), depositUserRequest).blockingGet());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    private UserResponse createUser(String name) {
        CreateUserRequest request = new CreateUserRequest(name);
        return restClient.createUser(request).blockingGet();
    }
}