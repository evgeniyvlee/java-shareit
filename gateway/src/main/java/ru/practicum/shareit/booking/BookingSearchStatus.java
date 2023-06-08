package ru.practicum.shareit.booking;

/**
 * Booking status
 * @author Evgeniy Lee
 */
public enum BookingSearchStatus {
    ALL("Все"),
    CURRENT("Текущие"),
    PAST("Прошлые"),
    FUTURE("Будущие"),
    WAITING("Новые/Ожидают бронирования"),
    REJECTED("Отмененные"),
    UNSUPPORTED_STATUS("Статус неопределен");

    private String name;

    private BookingSearchStatus(final String name) {
        this.name = name;
    }
}
