package MoneyTransfer.service;

import MoneyTransfer.db.ConnectionAdaptor;
import MoneyTransfer.db.Transaction;
import MoneyTransfer.db.dao.UserDao;
import MoneyTransfer.db.model.User;
import MoneyTransfer.rest.model.req.CreateUserRequest;
import MoneyTransfer.rest.model.req.DepositUserRequest;
import MoneyTransfer.rest.model.res.UserResponse;
import MoneyTransfer.service.mapper.UserMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private ConnectionAdaptor connectionAdaptor;

    @Inject
    public UserServiceImpl(UserDao userDao, ConnectionAdaptor connectionAdaptor) {
        this.connectionAdaptor = connectionAdaptor;
        this.userDao = userDao;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        User user = new User(request.getName());
        return UserMapper.adaprToResp(
                new Transaction<>(connectionAdaptor, () -> userDao.createUser(user)).execute()
        );
    }

    @Override
    public List<UserResponse> getAll() {
        return new Transaction<>(connectionAdaptor, () -> userDao.getAll()).execute()
                .stream().map(UserMapper::adaprToResp).collect(Collectors.toList());
    }

    @Override
    public UserResponse getById(long id) {
        return UserMapper.adaprToResp(
                new Transaction<>(connectionAdaptor, () -> userDao.getUserById(id)).execute()
        );
    }

    @Override
    public void deleteById(long id) {
        new Transaction<Void>(connectionAdaptor, () -> {
            userDao.deleteById(id);
            return null;
        }).execute();
    }

    @Override
    public void deleteAll() {
        new Transaction<Void>(connectionAdaptor, () -> {
            userDao.deleteAll();
            return null;
        }).execute();
    }

    @Override
    public UserResponse addMoney(long id, DepositUserRequest request) {
        return UserMapper.adaprToResp(
                new Transaction<>(connectionAdaptor, () -> {
                    User user = userDao.getUserById(id);
                    User updateUser = new User(user.getId(), user.getName(), user.getCents() + request.getCountCents());
                    userDao.update(updateUser);
                    return updateUser;
                }).execute()
        );
    }


}
