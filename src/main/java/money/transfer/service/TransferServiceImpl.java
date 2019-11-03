package money.transfer.service;

import money.transfer.dao.TransferDao;
import money.transfer.dao.UserDao;
import money.transfer.dao.db.ConnectionAdaptor;
import money.transfer.dao.db.Transaction;
import money.transfer.dao.exception.TransferException;
import money.transfer.dao.model.User;
import money.transfer.rest.model.req.TransferRequest;
import money.transfer.rest.model.res.TransferResponse;
import money.transfer.service.mapper.TransferMapper;
import money.transfer.service.validate.TransferValidate;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransferServiceImpl implements TransferService {

    private final UserDao userDao;
    private final TransferDao transferDao;
    private ConnectionAdaptor connectionAdaptor;

    @Inject
    public TransferServiceImpl(UserDao userDao, TransferDao transferDao, ConnectionAdaptor connectionAdaptor) {
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.connectionAdaptor = connectionAdaptor;
    }

    @Override
    public TransferResponse transferMoney(long fromUserId, long toUserId, TransferRequest request) {
        TransferValidate.validate(fromUserId, toUserId, request.getCount());
        return TransferMapper.adaprToResp(
                new Transaction<>(connectionAdaptor, () -> {
                    User from = userDao.getUserById(fromUserId);
                    User to = userDao.getUserById(toUserId);
                    if (from.getCents() >= request.getCount()) {
                        User updateUserFrom = new User(from.getId(), from.getName(), from.getCents() - request.getCount());
                        User updateUserTo = new User(to.getId(), to.getName(), to.getCents() + request.getCount());
                        userDao.update(updateUserFrom);
                        userDao.update(updateUserTo);
                        return transferDao.create(fromUserId, toUserId, request.getCount());
                    } else {
                        throw new TransferException("Insufficient funds for transfer money from userId:" + from.getId());
                    }
                }).execute()
        );
    }

    @Override
    public void deleteAll() {
        new Transaction<Void>(connectionAdaptor, () -> {
            transferDao.deleteAll();
            return null;
        }).execute();
    }
}
