package money.transfer.rest.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;
import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
import money.transfer.rest.model.res.UserResponse;
import money.transfer.service.UserService;

import javax.inject.Inject;
import java.util.List;

@Controller("/user")
@Validated
public class UserController {

    private final UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Post
    public HttpResponse<UserResponse> createUser(final CreateUserRequest request) {
        return HttpResponse.created(userService.createUser(request));
    }

    @Get(uri = "/all", produces = MediaType.APPLICATION_JSON)
    public List<UserResponse> getAllUsers() {
        return userService.getAll();
    }

    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    public UserResponse getUserById(final long id) {
        return userService.getById(id);
    }

    @Delete(uri = "/{id}")
    public HttpResponse deleteUser(final Long id) {
        userService.deleteById(id);
        return HttpResponse.noContent();
    }

    @Put(uri = "/{id}/deposit", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public UserResponse addMoney(final long id, final DepositUserRequest depositUserRequest) {
        return userService.addMoney(id, depositUserRequest);
    }

}
