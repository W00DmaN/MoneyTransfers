package money.transfer.rest.controller;

import io.micronaut.test.annotation.MicronautTest;
import money.transfer.RestClient;
import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
import money.transfer.rest.model.req.TransferRequest;
import money.transfer.rest.model.res.UserResponse;
import money.transfer.service.TransferService;
import money.transfer.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
class TransferControllerTest {

    @Inject
    private RestClient restClient;
    @Inject
    private UserService userService;
    @Inject
    private TransferService transferService;

    @AfterEach
    void after() {
        transferService.deleteAll();
        userService.deleteAll();
    }

    @Test
    void testTransferMoneyWithNotCreatedUsers() {
        TransferRequest request = new TransferRequest(100);
        assertThrows(Exception.class, () -> restClient.transferMoney(1L, 1L, request).blockingGet());
    }

    @Test
    void testTransferMoney() {

        long countCents = 100L;

        CreateUserRequest userReq1 = new CreateUserRequest("Tim2_Test");
        CreateUserRequest userReq2 = new CreateUserRequest("Tim2_Test");
        UserResponse userResp1 = restClient.createUser(userReq1).blockingGet();
        UserResponse userResp2 = restClient.createUser(userReq2).blockingGet();

        DepositUserRequest depositUserRequest = new DepositUserRequest(countCents);

        userResp1 = restClient.addMoney(userResp1.getId(), depositUserRequest).blockingGet();

        TransferRequest transferRequest = new TransferRequest(countCents);
        restClient.transferMoney(userResp1.getId(), userResp2.getId(), transferRequest).blockingGet();

        assertEquals(countCents, restClient.getUserById(userResp2.getId()).blockingGet().getCents());

    }


}