package MoneyTransfer.rest.controller;

import MoneyTransfer.RestClient;
import MoneyTransfer.rest.model.req.CreateUserRequest;
import MoneyTransfer.rest.model.res.UserResponse;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class UserControllerTest {

    @Inject
    RestClient restClient;

    @Test
    void createUser() {
        String userName = "Tim _test";
        CreateUserRequest req = new CreateUserRequest(userName);
        UserResponse resp = restClient.createUser(req).blockingGet();
        assertEquals(userName, resp.getName());
    }

    @Test
    void getAllUsers() {

    }
}