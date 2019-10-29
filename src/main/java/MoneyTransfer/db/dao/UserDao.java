package MoneyTransfer.db.dao;

import MoneyTransfer.db.model.User;

import java.util.List;

public interface UserDao {
    User createUser(User user);
    User getUserById(long id);
    List<User> getAll();
}
