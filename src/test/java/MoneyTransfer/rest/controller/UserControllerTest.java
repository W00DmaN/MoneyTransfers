package MoneyTransfer.rest.controller;

import MoneyTransfer.RestClient;
import MoneyTransfer.db.dao.UserDao;
import MoneyTransfer.db.exception.UserNotFoundException;
import MoneyTransfer.rest.model.req.CreateUserRequest;
import MoneyTransfer.rest.model.res.UserResponse;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class UserControllerTest {

    @Inject
    private RestClient restClient;
    @Inject
    private UserDao userDao;

    @AfterEach
    void after(){
        userDao.deleteAll();
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
        assertTrue(result.contains(userResponse1));
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
        assertThrows(Exception.class, () -> {
            restClient.getUserById(response.getId()).blockingGet();
        });
    }
}