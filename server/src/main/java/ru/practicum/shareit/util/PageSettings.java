package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageSettings extends PageRequest {
    public PageSettings(int from, int size, Sort sort) {
        super(from / size, size, sort);
    }
}
