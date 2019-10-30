package MoneyTransfer.service.handler;

import MoneyTransfer.db.exception.UserNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;

import javax.inject.Singleton;


@Produces
@Singleton
@Requires(classes = {UserNotFoundException.class, ExceptionHandler.class})
public class MoneyTransferExceptionHandler implements ExceptionHandler<UserNotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, UserNotFoundException exception) {
        return HttpResponse.notFound(new ErrorMessage(exception.getMessage(),exception.getClass().getName()));
    }
}
