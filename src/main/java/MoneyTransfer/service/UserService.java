package MoneyTransfer.service;

import MoneyTransfer.rest.model.req.CreateUserRequest;
import MoneyTransfer.rest.model.res.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAll();

    UserResponse getById(long id);

    void deleteById(long id);
}
