package money.transfer.dao;

import money.transfer.dao.model.User;

import java.util.List;

public interface UserDao {
    User createUser(User user);

    User getUserById(long id);

    User getUserByIdWithLock(long id);

    void deleteById(long id);

    void deleteAll();

    List<User> getAll();

    User update(User user);
}
