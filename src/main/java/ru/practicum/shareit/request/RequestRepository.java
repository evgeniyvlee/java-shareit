package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterId(Long requesterId, Pageable pageable);

    List<ItemRequest> findAllByRequesterIdNot(Long requesterId, Pageable pageable);

    Sort SORT_CREATE_DATE_ASC = Sort.by(Sort.Direction.ASC, "created");
}
