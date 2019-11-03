package money.transfer.dao.db;

import io.micronaut.runtime.context.scope.ThreadLocal;
import money.transfer.dao.exception.MoneyTransferDaoException;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ThreadLocal
public class ConnectionAdaptor {
    private final DataSource dataSource;
    private Connection holder;

    @Inject
    public ConnectionAdaptor(DataSourceFactory dataSource) {
        this.dataSource = dataSource.getDataSource();
        this.holder = null;
    }

    public Connection getConnection() {
        if (holder == null) {
            try {
                holder = dataSource.getConnection();
            } catch (SQLException e) {
                throw new MoneyTransferDaoException(e);
            }
        }
        return holder;
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new MoneyTransferDaoException(e);
        } finally {
            holder = null;
        }
    }
}
