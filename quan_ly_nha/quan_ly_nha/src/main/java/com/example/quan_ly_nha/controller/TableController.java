package com.example.quan_ly_nha.controller;


import com.example.quan_ly_nha.dto.TableDTO;
import com.example.quan_ly_nha.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final RestaurantService restaurantService;

    @Autowired
    public TableController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<TableDTO>> getAllTables() {
        return ResponseEntity.ok(restaurantService.getAllTables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableDTO> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getTableById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<TableDTO>> getAvailableTables(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") int capacity) {
        return ResponseEntity.ok(restaurantService.getAvailableTables(startTime, endTime, capacity));
    }
}
