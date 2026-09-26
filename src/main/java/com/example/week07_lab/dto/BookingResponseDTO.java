package com.example.week07_lab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Long flightId;
    private String flightNumber;
    private LocalDateTime bookingDate;
}
