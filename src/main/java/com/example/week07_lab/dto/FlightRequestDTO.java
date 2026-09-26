package com.example.week07_lab.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequestDTO {

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]{1,6}$")
    private String flightNumber;

    @NotBlank
    private String airline;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotNull
    @Positive
    private Integer availableSeats;

    @AssertTrue(message = "La hora de salida debe ser anterior a la hora de llegada")
    public boolean isScheduleValid() {
        if (departureTime == null || arrivalTime == null) return true; // eso ya lo valida @NotNull
        return departureTime.isBefore(arrivalTime);
    }
}
