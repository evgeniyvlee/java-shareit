package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.assertj.core.api.Assertions;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    JacksonTester<ItemRequestDto> json;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS");

    @Test
    public void dtoToJsonTest() throws IOException {
        ItemRequestDto requestDto = new ItemRequestDto();
        LocalDateTime now = LocalDateTime.now();
        requestDto.setId(1L);
        requestDto.setDescription("Item request 1 description");
        requestDto.setCreated(now);

        JsonContent<ItemRequestDto> content = json.write(requestDto);
        Assertions.assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(content)
                .extractingJsonPathStringValue("$.description").isEqualTo(requestDto.getDescription());
        Assertions.assertThat(content)
                .extractingJsonPathStringValue("$.created").isEqualTo(now.format(formatter));
    }
}
