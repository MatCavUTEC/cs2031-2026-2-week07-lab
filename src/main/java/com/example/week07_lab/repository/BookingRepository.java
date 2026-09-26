package com.example.week07_lab.repository;

import com.example.week07_lab.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Dos vuelos se cruzan si cada uno empieza antes de que termine el otro
    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.user.id = :userId
              AND b.flight.departureTime < :arrival
              AND b.flight.arrivalTime > :departure
            """)
    boolean existsScheduleConflict(@Param("userId") Long userId,
                                   @Param("departure") LocalDateTime departure,
                                   @Param("arrival") LocalDateTime arrival);
}
