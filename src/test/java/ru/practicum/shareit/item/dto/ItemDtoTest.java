package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.assertj.core.api.Assertions;
import java.io.IOException;

@JsonTest
public class ItemDtoTest {
    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    public void dtoToJsonTest() throws IOException {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item 1");
        itemDto.setDescription("Item 1 description");
        itemDto.setAvailable(true);

        JsonContent<ItemDto> content = json.write(itemDto);
        Assertions.assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        Assertions.assertThat(content)
                .extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        Assertions.assertThat(content)
                .extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
    }
}
