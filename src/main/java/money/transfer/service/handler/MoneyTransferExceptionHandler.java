package money.transfer.service.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import money.transfer.dao.exception.UserNotFoundException;
import money.transfer.service.exception.TransferInvalideSummException;
import money.transfer.service.exception.ValidateTransferException;
import money.transfer.service.exception.ValidateUserException;

import javax.inject.Singleton;


@Produces
@Singleton
@Requires(classes = {UserNotFoundException.class, ValidateTransferException.class, ExceptionHandler.class})
public class MoneyTransferExceptionHandler implements ExceptionHandler<Throwable, HttpResponse> {


    @Override
    public HttpResponse handle(HttpRequest request, Throwable e) {
        if (e instanceof UserNotFoundException) {
            return HttpResponse.notFound(new ErrorMessage(e.getMessage(), e.getClass().getName()));
        }
        if (e instanceof ValidateTransferException) {
            return HttpResponse.badRequest(new ErrorMessage(e.getMessage(), e.getClass().getName()));
        }
        if (e instanceof ValidateUserException) {
            return HttpResponse.badRequest(new ErrorMessage(e.getMessage(), e.getClass().getName()));
        }
        if (e instanceof TransferInvalideSummException) {
            return HttpResponse.badRequest(new ErrorMessage(e.getMessage(), e.getClass().getName()));
        }

        return HttpResponse.serverError(e);
    }
}
