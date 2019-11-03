package money.transfer.dao.db;

import money.transfer.dao.TransferDao;
import money.transfer.dao.exception.CreateTransferException;
import money.transfer.dao.exception.TransferException;
import money.transfer.dao.exception.UserException;
import money.transfer.dao.model.Transfer;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransferDaoImpl implements TransferDao {

    private final ConnectionAdaptor connectionAdaptor;

    private static final String CREATE_TRANSFER = "INSERT INTO transfer (from_user_id, to_user_id, summ) VALUES (?, ?, ?)";
    private static final String GET_BY_ID = "SELECT id, from_user_id, to_user_id, summ FROM transfer WHERE id = ?";
    private static final String GET_ALL_TRANSFERS = "SELECT id, from_user_id, to_user_id, summ FROM transfer";
    private static final String DELETE_ALL = "DELETE FROM transfer";

    @Inject
    public TransferDaoImpl(ConnectionAdaptor connectionAdaptor) {
        this.connectionAdaptor = connectionAdaptor;
    }

    @Override
    public Transfer create(long fromUserId, long toUserId, long count) {
        return execute(CREATE_TRANSFER, statement -> {
            statement.setLong(1, fromUserId);
            statement.setLong(2, toUserId);
            statement.setLong(3, count);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new CreateTransferException("Creating transfer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Transfer(generatedKeys.getLong(1), fromUserId, toUserId, count);
                } else {
                    throw new CreateTransferException("Creating transfer failed, no ID obtained.");
                }
            }
        });
    }

    @Override
    public Transfer getById(long transferId) {
        return execute(GET_BY_ID, statement -> {
            statement.setLong(1, transferId);
            statement.execute();
            Transfer result;
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    result = getTransferFromResultSet(resultSet);
                } else {
                    throw new TransferException("Transfer with id = " + transferId + "not found");
                }
            }
            return result;
        });
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return execute(GET_ALL_TRANSFERS, statement -> {
            statement.execute();
            List<Transfer> result = new ArrayList<>();
            try (ResultSet resultSet = statement.getResultSet()) {
                while (resultSet.next()) {
                    result.add(getTransferFromResultSet(resultSet));
                }
            }
            return result;
        });
    }

    @Override
    public void deleteAll() {
        execute(DELETE_ALL, PreparedStatement::execute);
    }

    private Transfer getTransferFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long fromUserId = resultSet.getLong("from_user_id");
        long toUserId = resultSet.getLong("to_user_id");
        long summ = resultSet.getLong("summ");
        return new Transfer(id, fromUserId, toUserId, summ);
    }

    @FunctionalInterface
    private interface StatementCallable<T> {
        T call(PreparedStatement statement) throws SQLException;
    }

    private <T> T execute(String query, StatementCallable<T> callable) {
        Connection connection = connectionAdaptor.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return callable.call(statement);
        } catch (SQLException e) {
            throw new UserException(e);
        }
    }
}
