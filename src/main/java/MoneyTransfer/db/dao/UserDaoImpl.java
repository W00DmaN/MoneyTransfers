package MoneyTransfer.db.dao;

import MoneyTransfer.db.DataSourceFactory;
import MoneyTransfer.db.exception.UserNotFoundException;
import MoneyTransfer.db.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserDaoImpl implements UserDao {
    private final DataSourceFactory sourceFactory;

    private static final String CREATE_USER = "INSERT INTO user (id, name, cents) VALUES (?, ?, ?)";
    private static final String GET_ALL = "SELECT id, name, cents FROM user";
    private static final String GET_BY_ID = "SELECT id, name, cents FROM user WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM user WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM user";

    @Inject
    public UserDaoImpl(DataSourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public User createUser(User user) {
        return execute(CREATE_USER, statement -> {
            statement.setLong(1,user.getId());
            statement.setString(2,user.getName());
            statement.setBigDecimal(3, user.getCents());
            statement.execute();
            return user;
        });
    }

    @Override
    public User getUserById(long id) {
        return execute(GET_BY_ID , statement -> {
            statement.setLong(1, id);
            statement.execute();
            User result;
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    long userId = resultSet.getLong("id");
                    String userName = resultSet.getString("name");
                    BigDecimal cents = resultSet.getBigDecimal("cents");
                    result = new User(userId, userName,cents);
                } else {
                    throw new UserNotFoundException("User with id="+id+" not found");
                }
            }
            return  result;
        });
    }

    @Override
    public void deleteById(long id) {
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
            try (ResultSet resultSet = statement.getResultSet()){
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    BigDecimal bigDecimal = resultSet.getBigDecimal("cents");
                    result.add(new User(id,name,bigDecimal));
                }
            }
            return result;
        });
    }


    @FunctionalInterface
    private interface StatementCallable<T> {
        T call(PreparedStatement statement) throws SQLException;
    }

    private <T> T execute(String query, StatementCallable<T> callable) {
        try(Connection connection = sourceFactory.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            return callable.call(statement);
        } catch (SQLException e) {
            throw new UserNotFoundException(e);
        }
    }
}
