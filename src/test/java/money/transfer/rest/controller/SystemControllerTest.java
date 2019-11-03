package money.transfer.rest.controller;

import io.micronaut.test.annotation.MicronautTest;
import money.transfer.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class SystemControllerTest {

    @Inject
    RestClient restClient;

    @Test
    void testHealth() {
        assertEquals(
                "0",
                restClient.health().blockingGet());
    }
}
