package money.transfer.service.mapper;

import money.transfer.dao.model.User;
import money.transfer.rest.model.res.UserResponse;

public final class UserMapper {
    public static UserResponse adaprToResp(final User user) {
        return new UserResponse(user.getId(), user.getName(), user.getCents());
    }
}
