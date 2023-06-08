package ru.practicum.shareit.booking.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonTest
public class BriefBookingDtoTest {
    @Autowired
    JacksonTester<BriefBookingDto> json;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    @Test
    public void dtoToJsonTest() throws IOException {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        BriefBookingDto briefBookingDto = new BriefBookingDto();
        briefBookingDto.setId(1L);
        briefBookingDto.setItemId(2L);
        briefBookingDto.setBookerId(3L);
        briefBookingDto.setStart(start);
        briefBookingDto.setEnd(end);
        briefBookingDto.setStatus(BookingStatus.APPROVED);

        JsonContent<BriefBookingDto> content = json.write(briefBookingDto);
        Assertions.assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(content).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        Assertions.assertThat(content).extractingJsonPathNumberValue("$.bookerId").isEqualTo(3);
        Assertions.assertThat(content)
                .extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        Assertions.assertThat(content)
                .extractingJsonPathStringValue("$.start").isEqualTo(start.format(formatter));
        Assertions.assertThat(content)
                .extractingJsonPathStringValue("$.end").isEqualTo(end.format(formatter));
    }
}
