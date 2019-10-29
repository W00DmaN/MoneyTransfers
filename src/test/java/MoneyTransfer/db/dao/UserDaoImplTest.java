package MoneyTransfer.db.dao;

import MoneyTransfer.db.model.User;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class UserDaoImplTest {

    @Inject
    private UserDao userDao;

    @Test
    void createUser() {
        User user = new User("Tim_test");
        userDao.createUser(user);

    }

    @Test
    void getAll() {
    }
}