package ru.practicum.shareit.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import java.io.IOException;

@JsonTest
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    public void dtoToJsonTest() throws IOException {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("User 1");
        userDto.setEmail("userDto@yandex.ru");

        JsonContent<UserDto> content = json.write(userDto);
        Assertions.assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        Assertions.assertThat(content).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }
}
