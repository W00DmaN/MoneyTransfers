package money.transfer.rest.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import money.transfer.rest.model.req.TransferRequest;
import money.transfer.rest.model.res.TransferResponse;
import money.transfer.service.TransferService;

import javax.inject.Inject;

@Controller("/transfer")
public class TransferController {

    private final TransferService transferService;

    @Inject
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Post(uri = "/money/from/{userFromId}/to/{userToId}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public TransferResponse transferMoney(final long userFromId, final long userToId, final TransferRequest request) {
        return transferService.transferMoney(userFromId, userToId, request);
    }
}
