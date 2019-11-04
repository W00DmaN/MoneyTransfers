package money.transfer.dao.db;

import money.transfer.dao.UserDao;
import money.transfer.dao.exception.CreateUserException;
import money.transfer.dao.exception.UserException;
import money.transfer.dao.exception.UserNotFoundException;
import money.transfer.dao.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserDaoImpl implements UserDao {
    private final ConnectionAdaptor connectionAdaptor;

    private static final String CREATE_USER = "INSERT INTO user (name, cents) VALUES (?, ?)";
    private static final String GET_ALL = "SELECT id, name, cents FROM user";
    private static final String GET_BY_ID = "SELECT id, name, cents FROM user WHERE id = ?";
    private static final String GET_BY_ID_WITH_LOCK = "SELECT id, name, cents FROM user WHERE id = ? for update";
    private static final String DELETE_BY_ID = "DELETE FROM user WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM user";
    private static final String UPDATE_USER = "UPDATE user SET name=?, cents=? WHERE id=?";

    @Inject
    public UserDaoImpl(ConnectionAdaptor connectionAdaptor) {
        this.connectionAdaptor = connectionAdaptor;
    }

    @Override
    public User createUser(final User user) {
        return execute(CREATE_USER, statement -> {
            statement.setString(1, user.getName());
            statement.setLong(2, user.getCents());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new CreateUserException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new User(generatedKeys.getLong(1), user.getName(), user.getCents());
                } else {
                    throw new CreateUserException("Creating user failed, no ID obtained.");
                }
            }
        });
    }

    @Override
    public User getUserById(final long id) {
        return execute(GET_BY_ID, statement -> {
            statement.setLong(1, id);
            statement.execute();
            User result;
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    result = getUserFromResultSet(resultSet);
                } else {
                    throw new UserNotFoundException(String.format("User with id = %s not found", id));
                }
            }
            return result;
        });
    }

    @Override
    public User getUserByIdWithLock(final long id) {
        return execute(GET_BY_ID_WITH_LOCK, statement -> {
            statement.setLong(1, id);
            statement.execute();
            User result;
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    result = getUserFromResultSet(resultSet);
                } else {
                    throw new UserNotFoundException(String.format("User with id = %s not found", id));
                }
            }
            return result;
        });
    }

    @Override
    public void deleteById(final long id) {
        execute(DELETE_BY_ID, statement -> {
            statement.setLong(1, id);
            statement.execute();
            return null;
        });
    }

    @Override
    public void deleteAll() {
        execute(DELETE_ALL, statement -> {
            statement.execute();
            return null;
        });
    }

    @Override
    public List<User> getAll() {
        return execute(GET_ALL, statement -> {
            statement.execute();
            List<User> result = new ArrayList<>();
            try (ResultSet resultSet = statement.getResultSet()) {
                while (resultSet.next()) {
                    result.add(getUserFromResultSet(resultSet));
                }
            }
            return result;
        });
    }

    @Override
    public User update(final User user) {
        return execute(UPDATE_USER, statement -> {
            statement.setString(1, user.getName());
            statement.setLong(2, user.getCents());
            statement.setLong(3, user.getId());
            statement.execute();
            return user;
        });
    }


    private User getUserFromResultSet(final ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        long cents = resultSet.getLong("cents");
        return new User(id, name, cents);
    }

    @FunctionalInterface
    private interface StatementCallable<T> {
        T call(PreparedStatement statement) throws SQLException;
    }

    private <T> T execute(final String query, final StatementCallable<T> callable) {
        Connection connection = connectionAdaptor.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            return callable.call(statement);
        } catch (SQLException e) {
            throw new UserException(e);
        }
    }
}
