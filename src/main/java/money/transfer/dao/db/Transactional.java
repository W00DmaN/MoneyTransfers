package money.transfer.dao.db;

public interface Transactional<T> {
    T execute();
}
