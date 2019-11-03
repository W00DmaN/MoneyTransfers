package money.transfer.rest.controller;

import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.res.UserResponse;
import money.transfer.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerUnitTest {

    private UserService userService = mock(UserService.class);
    private UserController controller = new UserController(userService);

    private UserResponse userResponse = new UserResponse(1L, "Tim", 0l);

    @Test
    void testGetUserById() {
        when(userService.getById(userResponse.getId())).thenReturn(userResponse);
        UserResponse response = controller.getUserById(userResponse.getId());
        verify(userService).getById(eq(userResponse.getId()));
        assertEquals(userResponse, response);
    }

    @Test
    void testGetUsrAll() {
        when(userService.getAll()).thenReturn(Collections.singletonList(userResponse));
        List<UserResponse> responses = controller.getAllUsers();
        verify(userService).getAll();
        assertEquals(1, responses.size());
        assertEquals(userResponse, responses.get(0));
    }

    @Test
    void testDeleteById() {
        controller.deleteUser(userResponse.getId());
        verify(userService, times(1)).deleteById(eq(userResponse.getId()));
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any())).thenReturn(userResponse);
        CreateUserRequest createUserRequest = new CreateUserRequest(userResponse.getName());
        UserResponse response = controller.createUser(createUserRequest).body();
        verify(userService).createUser(eq(createUserRequest));
        assertEquals(userResponse, response);
    }
}
