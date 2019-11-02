package MoneyTransfer.db;

import MoneyTransfer.db.exception.MoneyTransferDaoException;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction<T> {
    private final ConnectionAdaptor connectionAdaptor;
    private final Transactional<T> transactional;

    public Transaction(ConnectionAdaptor connectionAdaptor, Transactional<T> transactional) {
        this.connectionAdaptor = connectionAdaptor;
        this.transactional = transactional;
    }

    public T execute() {
        Connection connection = connectionAdaptor.getConnection();
        try {
            try {
                connection.setAutoCommit(false);
                T result = transactional.execute();
                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connectionAdaptor.closeConnection(connection);
            }
        } catch (SQLException e) {
            throw new MoneyTransferDaoException(e);
        }
    }
}
