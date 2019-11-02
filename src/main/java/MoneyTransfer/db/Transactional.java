package MoneyTransfer.db;

public interface Transactional<T> {
    T execute();
}
