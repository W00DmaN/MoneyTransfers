package money.transfer.rest.controller;

import io.micronaut.http.client.exceptions.HttpClientResponseException;
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

        UserResponse userResp1 = createUserWithMoney("Tim1_Test", countCents);
        UserResponse userResp2 = createUserWithMoney("Tim2_Test", 0L);

        TransferRequest transferRequest = new TransferRequest(countCents);
        restClient.transferMoney(userResp1.getId(), userResp2.getId(), transferRequest).blockingGet();

        assertEquals(countCents, restClient.getUserById(userResp2.getId()).blockingGet().getCents());
    }

    @Test
    void testValidateTransferMoneyForUserId() {
        long countCents = 100L;

        UserResponse userResp = createUserWithMoney("Tim1_Test", countCents);

        TransferRequest transferRequest = new TransferRequest(countCents);
        assertThrows(HttpClientResponseException.class, () -> restClient.transferMoney(userResp.getId(), userResp.getId(), transferRequest).blockingGet());
    }

    @Test
    void testValidateNegativTransferSumm() {
        long countCents = 0L;

        UserResponse user1 = createUserWithMoney("Tim1_Test", countCents);
        UserResponse user2 = createUserWithMoney("Tim2_Test", 0L);

        TransferRequest transferRequest = new TransferRequest(-1L);
        assertThrows(HttpClientResponseException.class, () -> restClient.transferMoney(user1.getId(), user2.getId(), transferRequest).blockingGet());
    }

    private UserResponse createUserWithMoney(String userName, long summ) {
        CreateUserRequest userReq = new CreateUserRequest("Tim1_Test");
        UserResponse userResp = restClient.createUser(userReq).blockingGet();

        DepositUserRequest depositUserRequest = new DepositUserRequest(summ);
        return restClient.addMoney(userResp.getId(), depositUserRequest).blockingGet();
    }
}