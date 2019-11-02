package MoneyTransfer.db.dao;

import MoneyTransfer.db.model.User;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class UserDaoImplTest {

    @Inject
    private UserDao userDao;

    @AfterEach
    void afterTest() {
        userDao.deleteAll();
    }

    @Test
    void createUser() {
        User user = new User("Tim_test");
        User result = userDao.createUser(user);
        assertEquals(result, userDao.getUserById(result.getId()));
    }

    @Test
    void userGetById() {
        User user = new User("Test1");
        User user2 = new User("Test2");

        user = userDao.createUser(user);
        user2 = userDao.createUser(user2);

        assertEquals(user.getName(), userDao.getUserById(user.getId()).getName());
        assertEquals(user2.getName(), userDao.getUserById(user2.getId()).getName());
    }

    @Test
    void getAll() {
        User user = new User("Tim_test");
        User user2 = new User("Tim1_test");

        user = userDao.createUser(user);
        user2 = userDao.createUser(user2);

        List<User> results = userDao.getAll();

        assertEquals(2, results.size());
        assertTrue(results.contains(user));
        assertTrue(results.contains(user2));
    }
}