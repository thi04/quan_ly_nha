package com.example.quan_ly_nha.repository;

import com.example.quan_ly_nha.model.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByTableId(Long tableId);
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);
}