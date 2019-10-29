package MoneyTransfer.rest.controller;

import MoneyTransfer.db.dao.UserDao;
import MoneyTransfer.rest.model.req.CreateUserRequest;
import MoneyTransfer.rest.model.res.UserResponse;
import MoneyTransfer.service.UserService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;

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
    public UserResponse createUser(CreateUserRequest request){
        return userService.createUser(request);
    }

    @Get(uri = "/all")
    public List<UserResponse> getAllUsers(){
        return userService.getAll();
    }

    @Get(uri = "/{id}")
    public UserResponse getUserById(long id){
        return userService.getById(id);
    }

    @Delete(uri = "/{id}")
    public void deleteUser(Long id) {
    }

}
