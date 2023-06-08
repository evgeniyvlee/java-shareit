package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Booking repository
 * @author Evgeniy Lee
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long bookerId, Pageable pageable);

    @Query(value = "SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.booker.id = :bookerId AND booking.start < :date AND booking.end > :date")
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(@Param("bookerId") Long bookerId,
                                                             @Param("date") LocalDateTime date, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItemOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.item.owner.id = :ownerId AND booking.start < :date AND booking.end > :date")
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(@Param("ownerId") Long ownerId,
                                                                @Param("date") LocalDateTime date, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(Long bookerId, Long itemId, LocalDateTime end,
                                                                  BookingStatus status, Sort sort);

    @Query(value = "SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.item.id IN :itemIds AND booking.status = 'APPROVED' ")
    List<Booking> findAllByItemIdIn(@Param("itemIds") List<Long> itemIds);

    Sort SORT_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "start");
}
