package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    public void getUnknownUserTest() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenThrow(new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        DataNotFoundException exception = Assertions.assertThrows(DataNotFoundException.class, () -> service.get(99L));

        Assertions.assertEquals(ExceptionMessages.DATA_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void updateUnknownUserTest() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenThrow(new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        DataNotFoundException exception = Assertions
                .assertThrows(DataNotFoundException.class, () -> service.update(99L, new UserDto()));

        Assertions.assertEquals(ExceptionMessages.DATA_NOT_FOUND, exception.getMessage());

    }
}
