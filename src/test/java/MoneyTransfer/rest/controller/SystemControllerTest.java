package MoneyTransfer.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import MoneyTransfer.RestClient;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

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
