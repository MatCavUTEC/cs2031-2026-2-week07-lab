package com.example.week07_lab.service;

import com.example.week07_lab.model.Booking;
import com.example.week07_lab.model.Flight;
import com.example.week07_lab.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    // ISO_LOCAL_DATE_TIME siempre incluye segundos (2027-06-01T10:00:00);
    // LocalDateTime.toString() los omite cuando son :00 (2027-06-01T10:00)
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void sendConfirmation(Booking booking) {
        User user = booking.getUser();
        Flight flight = booking.getFlight();

        String content = """
                Para: %s
                Asunto: Confirmación de reserva #%d

                Hola %s %s, tu reserva fue confirmada.

                Pasajero: %s %s
                Número de vuelo: %s
                Aerolínea: %s
                Salida: %s
                Llegada: %s
                Fecha de reserva: %s
                """.formatted(
                user.getEmail(), booking.getId(),
                user.getFirstName(), user.getLastName(),
                user.getFirstName(), user.getLastName(),
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getDepartureTime().format(ISO),
                flight.getArrivalTime().format(ISO),
                booking.getBookingDate().format(ISO));

        try {
            Files.writeString(Path.of("flight_booking_email_" + booking.getId() + ".txt"), content);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo generar el email de confirmación", e);
        }
    }
}
