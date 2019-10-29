package MoneyTransfer.service;

import MoneyTransfer.db.dao.UserDao;
import MoneyTransfer.db.model.User;
import MoneyTransfer.rest.model.req.CreateUserRequest;
import MoneyTransfer.rest.model.res.UserResponse;
import MoneyTransfer.service.mapper.UserMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Inject
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        User user = new User(request.getName());
        return UserMapper.adaprToResp(userDao.createUser(user));
    }

    @Override
    public List<UserResponse> getAll() {
        return userDao.getAll().stream().map(UserMapper::adaprToResp).collect(Collectors.toList());
    }

    @Override
    public UserResponse getById(long id) {
        return UserMapper.adaprToResp(userDao.getUserById(id));
    }

    @Override
    public void deleteById(long id) {
        userDao.deleteById(id);
    }


}
