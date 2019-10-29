package MoneyTransfer.db.dao;

import MoneyTransfer.db.DataSourceFactory;
import MoneyTransfer.db.exception.UserDaoException;
import MoneyTransfer.db.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserDaoImpl implements UserDao {
    private final DataSourceFactory sourceFactory;

    private static final String CREATE_USER = "INSERT INTO user (id, name) VALUES (?, ?)";
    private static final String GET_ALL = "SELECT id, name FROM user";
    private static final String GET_BY_ID = "SELECT id, name FROM user WHERE id = ?";

    @Inject
    public UserDaoImpl(DataSourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public User createUser(User user) {
        return execute(CREATE_USER, statement -> {
            statement.setLong(1,user.getId());
            statement.setString(2,user.getName());
            statement.execute();
            return user;
        });
    }

    @Override
    public User getUserById(long id) {
        return execute(GET_BY_ID , statement -> {
            statement.setLong(1, id);
            statement.execute();
            User result = null;
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    long userId = resultSet.getLong("id");
                    String userName = resultSet.getString("name");
                    result = new User(userId, userName);
                }
            }
            return  result;
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
                    result.add(new User(id,name));
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
            throw new UserDaoException(e);
        }
    }
}
