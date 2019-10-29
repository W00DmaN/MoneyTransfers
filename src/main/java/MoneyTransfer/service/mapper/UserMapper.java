package MoneyTransfer.service.mapper;

import MoneyTransfer.db.model.User;
import MoneyTransfer.rest.model.res.UserResponse;

public final class UserMapper {
    public static UserResponse adaprToResp(User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
