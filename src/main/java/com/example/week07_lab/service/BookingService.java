package com.example.week07_lab.service;

import com.example.week07_lab.dto.BookingRequestDTO;
import com.example.week07_lab.dto.BookingResponseDTO;
import com.example.week07_lab.exception.BadRequestException;
import com.example.week07_lab.exception.ConflictException;
import com.example.week07_lab.exception.NotFoundException;
import com.example.week07_lab.model.Booking;
import com.example.week07_lab.model.Flight;
import com.example.week07_lab.model.User;
import com.example.week07_lab.repository.BookingRepository;
import com.example.week07_lab.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.emailService = emailService;
    }

    @Transactional
    public BookingResponseDTO bookFlight(User user, BookingRequestDTO dto) {
        Flight flight = flightRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new NotFoundException("Vuelo no encontrado"));

        // Salida <= ahora => el vuelo ya partió (pasado) o está en el aire (en tránsito)
        if (!flight.getDepartureTime().isAfter(LocalDateTime.now()))
            throw new BadRequestException("El vuelo ya partió o está en tránsito");
        if (flight.getAvailableSeats() <= 0)
            throw new ConflictException("No hay asientos disponibles");
        if (bookingRepository.existsScheduleConflict(user.getId(), flight.getDepartureTime(), flight.getArrivalTime()))
            throw new ConflictException("Tienes otra reserva que se cruza en horario");

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // Se genera después del save porque el nombre del archivo necesita el id de la reserva
        emailService.sendConfirmation(booking);
        return toDTO(booking);
    }

    public BookingResponseDTO getBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));
        return toDTO(booking);
    }

    private BookingResponseDTO toDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getFirstName(),
                booking.getUser().getLastName(),
                booking.getFlight().getId(),
                booking.getFlight().getFlightNumber(),
                booking.getBookingDate());
    }
}
