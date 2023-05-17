package ru.practicum.shareit.booking;

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

    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    @Query(value = "SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.booker.id = :bookerId AND booking.start < :date AND booking.end > :date")
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(@Param("bookerId") Long bookerId,
                                                             @Param("date") LocalDateTime date, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByItemOwnerId(Long ownerId, Sort sort);

    @Query(value = "SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.item.owner.id = :ownerId AND booking.start < :date AND booking.end > :date")
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(@Param("ownerId") Long ownerId,
                                                                @Param("date") LocalDateTime date, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(Long bookerId, Long itemId, LocalDateTime end,
                                                                  BookingStatus status, Sort sort);

    @Query(value = "SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.item.id IN :itemIds AND booking.status = 'APPROVED' ")
    List<Booking> findAllByItemIdIn(@Param("itemIds") List<Long> itemIds);

    Sort SORT_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "start");
}
