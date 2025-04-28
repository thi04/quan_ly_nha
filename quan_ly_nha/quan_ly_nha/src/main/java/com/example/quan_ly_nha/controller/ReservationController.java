package com.example.quan_ly_nha.controller;

import com.example.quan_ly_nha.dto.ReservationDTO;
import com.example.quan_ly_nha.service.RestaurantService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final RestaurantService restaurantService;

    @Autowired
    public ReservationController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        return new ResponseEntity<>(restaurantService.createReservation(reservationDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(restaurantService.updateReservationStatus(id, status));
    }
}