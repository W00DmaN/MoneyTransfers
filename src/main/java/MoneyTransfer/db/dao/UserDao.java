package MoneyTransfer.db.dao;

import MoneyTransfer.db.model.User;

import java.util.List;

public interface UserDao {
    User createUser(User user);

    User getUserById(long id);

    void deleteById(long id);

    void deleteAll();

    List<User> getAll();

    User update(User user);
}
