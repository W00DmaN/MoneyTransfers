package MoneyTransfer.rest.controller;

import MoneyTransfer.rest.model.req.TransferRequest;
import MoneyTransfer.rest.model.res.TransferResponse;
import MoneyTransfer.service.TransferService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;

@Controller("/transfer")
public class TransferController {

    private final TransferService transferService;

    @Inject
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Post(uri = "/money/from/{userFromId}/to/{userToId}")
    public TransferResponse transferMoney(long userFromId, long userToId, TransferRequest request) {
        return transferService.transferMoney(userFromId, userToId, request);
    }
}
