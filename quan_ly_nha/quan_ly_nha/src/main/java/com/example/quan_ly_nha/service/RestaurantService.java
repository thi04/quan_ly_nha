package com.example.quan_ly_nha.service;

import com.example.quan_ly_nha.dto.ReservationDTO;
import com.example.quan_ly_nha.dto.TableDTO;
import com.example.quan_ly_nha.exception.ResourceNotFoundException;
import com.example.quan_ly_nha.model.Reservation;
import com.example.quan_ly_nha.model.Table;
import com.example.quan_ly_nha.repository.ReservationRepository;
import com.example.quan_ly_nha.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public RestaurantService(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<TableDTO> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TableDTO getTableById(Long id) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        return convertToDTO(table);
    }

    public List<TableDTO> getAvailableTables(LocalDateTime startTime, LocalDateTime endTime, int capacity) {
        List<Table> availableTables = tableRepository.findAvailableTables(startTime, endTime, capacity);
        return availableTables.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Table table = tableRepository.findById(reservationDTO.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + reservationDTO.getTableId()));

        // Check if table is available for the requested time
        boolean isAvailable = reservationRepository.findOverlappingReservations(
                table.getId(),
                reservationDTO.getStartTime(),
                reservationDTO.getEndTime()
        ).isEmpty();

        if (!isAvailable) {
            throw new IllegalStateException("Table is not available for the requested time period");
        }

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setCustomerName(reservationDTO.getCustomerName());
        reservation.setCustomerPhone(reservationDTO.getCustomerPhone());
        reservation.setCustomerEmail(reservationDTO.getCustomerEmail());
        reservation.setPartySize(reservationDTO.getPartySize());
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setSpecialRequests(reservationDTO.getSpecialRequests());
        reservation.setStatus("CONFIRMED");

        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDTO(savedReservation);
    }

    @Transactional
    public ReservationDTO updateReservationStatus(Long id, String status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        reservation.setStatus(status);
        return convertToDTO(reservationRepository.save(reservation));
    }

    private TableDTO convertToDTO(Table table) {
        TableDTO dto = new TableDTO();
        dto.setId(table.getId());
        dto.setTableNumber(table.getTableNumber());
        dto.setCapacity(table.getCapacity());
        dto.setLocation(table.getLocation());
        dto.setStatus(table.getStatus());
        return dto;
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setTableId(reservation.getTable().getId());
        dto.setCustomerName(reservation.getCustomerName());
        dto.setCustomerPhone(reservation.getCustomerPhone());
        dto.setCustomerEmail(reservation.getCustomerEmail());
        dto.setPartySize(reservation.getPartySize());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setSpecialRequests(reservation.getSpecialRequests());
        dto.setStatus(reservation.getStatus());
        return dto;
    }
}
