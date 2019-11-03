package money.transfer.rest.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

@Controller
@Validated
public class SystemController {

    @Get(uri = "/health", produces = MediaType.TEXT_PLAIN)
    public Single<String> health() {
        return Single.just("0");
    }
}
