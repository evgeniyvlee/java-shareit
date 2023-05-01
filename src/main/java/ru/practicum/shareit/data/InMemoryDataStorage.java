package ru.practicum.shareit.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data storage holds data in map
 * @param <T>  data type with ID
 * @author Evgeniy Lee
 */
public abstract class InMemoryDataStorage<T extends Data> implements DataStorage<T> {
    // Storage in map
    protected final Map<Long, T> storage = new HashMap<>();
    // ids holds next id for data
    private long ids = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public T create(final T data) {
        data.setId(ids++);
        storage.put(data.getId(), data);
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(final Long id) {
        return storage.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() {
        return Collections.unmodifiableList(storage.values().stream().collect(Collectors.toList()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(final T data) {
        final long id = data.getId();
        storage.put(id, data);
        return storage.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long id) {
        storage.remove(id);
    }
}
