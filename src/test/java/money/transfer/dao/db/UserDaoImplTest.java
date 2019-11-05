package money.transfer.dao.db;

import io.micronaut.test.annotation.MicronautTest;
import money.transfer.dao.UserDao;
import money.transfer.dao.exception.UserNotFoundException;
import money.transfer.dao.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void positiveCreateUser() {
        User user = new User("Tim_test");
        User result = userDao.createUser(user);
        Assertions.assertEquals(result, userDao.getUserById(result.getId()));
    }

    @Test
    void positiveUserGetById() {
        User user = new User("Test1");
        User user2 = new User("Test2");

        user = userDao.createUser(user);
        user2 = userDao.createUser(user2);

        Assertions.assertEquals(user.getName(), userDao.getUserById(user.getId()).getName());
        Assertions.assertEquals(user2.getName(), userDao.getUserById(user2.getId()).getName());
    }

    @Test
    void userGetByIdForNotExistUser() {
        assertThrows(UserNotFoundException.class, () -> userDao.getUserById(-1L));
    }

    @Test
    void userGetByIdWithLock() {
        User user = new User("Test1");
        User user2 = new User("Test2");

        user = userDao.createUser(user);
        user2 = userDao.createUser(user2);

        Assertions.assertEquals(user.getName(), userDao.getUserByIdWithLock(user.getId()).getName());
        Assertions.assertEquals(user2.getName(), userDao.getUserByIdWithLock(user2.getId()).getName());
    }

    @Test
    void userGetByIdWithLockForNotExistUser() {
        assertThrows(UserNotFoundException.class, () -> userDao.getUserByIdWithLock(-1L));
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