package com.example.week07_lab.controller;

import com.example.week07_lab.dto.BookingRequestDTO;
import com.example.week07_lab.dto.BookingResponseDTO;
import com.example.week07_lab.model.User;
import com.example.week07_lab.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Sin @RequestMapping de clase: las rutas tienen prefijos distintos (/flights y /flight)
@RestController
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponseDTO> bookFlight(@AuthenticationPrincipal User user,
                                                         @Valid @RequestBody BookingRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookFlight(user, dto));
    }

    @GetMapping("/flight/book/{id}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }
}
