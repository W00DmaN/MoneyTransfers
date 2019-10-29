package MoneyTransfer.db.dao;

import MoneyTransfer.db.model.User;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class UserDaoImplTest {

    @Inject
    private UserDao userDao;

    @BeforeEach
    void before(){
        userDao.deleteAll();
    }

    @Test
    void createUser() {
        User user = new User("Tim_test");
        User result = userDao.createUser(user);
        assertEquals(result, userDao.getUserById(result.getId()));
    }

    @Test
    void userGetById(){
        User user = new User("Test1");
        User user2 = new User("Test2");

        userDao.createUser(user);
        userDao.createUser(user2);

        assertEquals(user.getName(), userDao.getUserById(user.getId()).getName());
        assertEquals(user2.getName(), userDao.getUserById(user2.getId()).getName());
    }

    @Test
    void getAll() {
        User user = new User("Tim_test");
        User user2 = new User("Tim1_test");
        User user3 = new User("Tim2_test");

        userDao.createUser(user);
        userDao.createUser(user2);
        userDao.createUser(user3);

        assertEquals(3, userDao.getAll().size());
    }
}