package money.transfer.rest.controller;

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
    void after(){
        userService.deleteAll();
    }

    @Test
    void createUser() {
        String userName = "Tim _test";
        CreateUserRequest req = new CreateUserRequest(userName);
        UserResponse resp = restClient.createUser(req).blockingGet();
        assertEquals(userName, resp.getName());
    }

    @Test
    void getAllUsers() {
        String userName = "Tim_test";
        String userName2 = "Tim2_test";
        CreateUserRequest req = new CreateUserRequest(userName);
        UserResponse userResponse1 = restClient.createUser(req).blockingGet();
        req = new CreateUserRequest(userName2);
        UserResponse userResponse2 = restClient.createUser(req).blockingGet();

        List<UserResponse> result = restClient.getAllUsers().blockingGet();

        assertEquals(2, result.size());
        assertTrue(result.contains(userResponse1), "getAllUser haven't user=" + userResponse1.toString());
        assertTrue(result.contains(userResponse2));
    }

    @Test
    void getAllUserFromEmpty() {
        List<UserResponse> responses = restClient.getAllUsers().blockingGet();
        assertEquals(0, responses.size());
    }

    @Test
    void getUserById() {
        String userName = "Tim_test";
        CreateUserRequest request = new CreateUserRequest(userName);
        UserResponse response = restClient.createUser(request).blockingGet();
        UserResponse result = restClient.getUserById(response.getId()).blockingGet();
        assertEquals(response, result);
    }

    @Test
    void deleteUserById() {
        String userName = "Tim_test";
        CreateUserRequest request = new CreateUserRequest(userName);
        UserResponse response = restClient.createUser(request).blockingGet();
        restClient.deleteUserById(response.getId()).blockingGet();
        assertThrows(HttpClientResponseException.class, () -> restClient.getUserById(response.getId()).blockingGet());
    }

    @Test
    void addMoney(){
        String userName = "Tim_test";
        CreateUserRequest request = new CreateUserRequest(userName);
        UserResponse response = restClient.createUser(request).blockingGet();
        assertEquals(0L, response.getCents());

        long depositSumm = 100L;
        DepositUserRequest depositUserRequest = new DepositUserRequest(depositSumm);
        restClient.addMoney(response.getId(), depositUserRequest).blockingGet();

        UserResponse result = restClient.getUserById(response.getId()).blockingGet();
        assertEquals(depositSumm, result.getCents());
    }
}