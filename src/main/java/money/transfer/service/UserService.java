package money.transfer.service;

import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
import money.transfer.rest.model.res.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAll();

    UserResponse getById(long id);

    void deleteAll();

    UserResponse addMoney(long id, DepositUserRequest depositUserRequest);
}
