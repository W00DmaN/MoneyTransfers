package MoneyTransfer.db;

import lombok.Getter;
import org.h2.jdbcx.JdbcDataSource;

import javax.inject.Singleton;

@Singleton
@Getter
public class DataSourceFactory {

    private final JdbcDataSource dataSource;

    public DataSourceFactory() {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:moneyTransfer;" +
                "DB_CLOSE_DELAY=-1;" +
                "INIT=RUNSCRIPT FROM 'classpath:scheme.sql'\\;");
        this.dataSource = jdbcDataSource;
    }
}
