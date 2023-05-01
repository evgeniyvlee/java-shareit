package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING(" Новое бронирование/Ожидает одобрения"),
    APPROVED("Бронирование подтверждено владельцем"),
    REJECTED("Бронирование отклонено владельцем"),
    CANCELED("Бронирование отменено создателем");

    private String name;

    private BookingStatus(final String name) {
        this.name = name;
    }
}
