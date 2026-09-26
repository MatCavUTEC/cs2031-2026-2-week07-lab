package com.example.week07_lab.service;

import com.example.week07_lab.dto.FlightRequestDTO;
import com.example.week07_lab.dto.FlightResponseDTO;
import com.example.week07_lab.exception.ConflictException;
import com.example.week07_lab.model.Flight;
import com.example.week07_lab.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    public FlightService(FlightRepository flightRepository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.flightRepository = flightRepository;
    }

    public FlightResponseDTO createFlight(FlightRequestDTO flightRequestDTO){
        if (flightRepository.existsByFlightNumber(flightRequestDTO.getFlightNumber()))
            throw new ConflictException("El número de vuelo ya existe");
        Flight newFlight = modelMapper.map(flightRequestDTO, Flight.class);
        newFlight = flightRepository.save(newFlight);
        return modelMapper.map(newFlight, FlightResponseDTO.class);
    }

    public List<FlightResponseDTO> searchFlights(String flightNumber, String airline, LocalDateTime from, LocalDateTime to) {
        // Filtros no enviados => valores que no filtran nada ("" coincide con todo en LIKE %%).
        // Se evita pasar null a la query porque PostgreSQL falla con parámetros null sin tipo.
        String number = flightNumber == null ? "" : flightNumber;
        String airlineName = airline == null ? "" : airline;
        LocalDateTime start = from == null ? LocalDateTime.of(1900, 1, 1, 0, 0) : from;
        LocalDateTime end = to == null ? LocalDateTime.of(3000, 1, 1, 0, 0) : to;

        return flightRepository
                .findByFlightNumberContainingIgnoreCaseAndAirlineContainingIgnoreCaseAndDepartureTimeBetween(number, airlineName, start, end)
                .stream()
                .map(flight -> modelMapper.map(flight, FlightResponseDTO.class))
                .toList();
    }
}
