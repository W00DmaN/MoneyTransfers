package money.transfer.rest.controller;

import io.micronaut.http.HttpStatus;
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
    void correctlyHandleExceptionTransferMoneyWithNotCreatedUserFrom() {
        TransferRequest request = new TransferRequest(100L);
        UserResponse userTo = createUserWithMoney("Tim1_Test", 100L);

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.transferMoney(-1L, userTo.getId(), request).blockingGet());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void correctlyHandleExceptionTransferMoneyWithNotCreatedUserTo() {
        TransferRequest request = new TransferRequest(100L);
        UserResponse userFrom = createUserWithMoney("Tim1_Test", 100L);

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.transferMoney(userFrom.getId(), -1L, request).blockingGet());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void positiveTransferMoney() {
        long countCents = 100L;

        UserResponse userFrom = createUserWithMoney("Tim1_Test", countCents);
        UserResponse userTo = createUserWithMoney("Tim2_Test", 0L);

        TransferRequest transferRequest = new TransferRequest(countCents);
        restClient.transferMoney(userFrom.getId(), userTo.getId(), transferRequest).blockingGet();

        assertEquals(countCents, restClient.getUserById(userTo.getId()).blockingGet().getCents());
        assertEquals(0L, restClient.getUserById(userFrom.getId()).blockingGet().getCents());
    }

    @Test
    void correctlyHandleExceptionTransferMoneyToMyself() {
        long countCents = 100L;

        UserResponse user = createUserWithMoney("Tim1_Test", countCents);

        TransferRequest transferRequest = new TransferRequest(countCents);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.transferMoney(user.getId(), user.getId(), transferRequest).blockingGet());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void correctlyHandleExceptionTransferMoneyWithNegativeSumm() {
        long countCents = 100L;

        UserResponse userFrom = createUserWithMoney("Tim1_Test", countCents);
        UserResponse userTo = createUserWithMoney("Tim2_Test", 0L);

        TransferRequest transferRequest = new TransferRequest(-1L);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.transferMoney(userFrom.getId(), userTo.getId(), transferRequest).blockingGet());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void correctlyHandleExceptionTransferMoneyInsufficientCountForUserFrom() {
        long countCents = 100L;

        UserResponse userFrom = createUserWithMoney("Tim1_Test", countCents);
        UserResponse userTo = createUserWithMoney("Tim2_Test", countCents);

        TransferRequest transferRequest = new TransferRequest(countCents + 1);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> restClient.transferMoney(userFrom.getId(), userTo.getId(), transferRequest).blockingGet());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    private UserResponse createUserWithMoney(String userName, long summ) {
        CreateUserRequest userReq = new CreateUserRequest("Tim1_Test");
        UserResponse userResp = restClient.createUser(userReq).blockingGet();

        if (summ < 1) return userResp;

        DepositUserRequest depositUserRequest = new DepositUserRequest(summ);
        return restClient.addMoney(userResp.getId(), depositUserRequest).blockingGet();
    }
}