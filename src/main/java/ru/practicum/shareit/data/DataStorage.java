package ru.practicum.shareit.data;

import java.util.List;

/**
 * Interface provides CRUD operations for data with ID
 * @param <T> data type with ID
 * @author Evgeniy Lee
 */
public interface DataStorage<T extends Data> {
    /**
     * Create data in storage
     * @param data data with ID
     * @return created data in storage
     */
    T create(T data);

    /**
     * Get data from storage by ID
     * @param id data ID
     * @return data from storage
     */
    T get(Long id);

    /**
     * Get all data from storage
     * @return list of all data
     */
    List<T> getAll();

    /**
     * Update data inn storage by ID
     * @param data updated data
     * @return updated data
     */
    T update(T data);

    /**
     * Delete data from storage by ID
     * @param id data ID
     */
    void delete(Long id);
}
