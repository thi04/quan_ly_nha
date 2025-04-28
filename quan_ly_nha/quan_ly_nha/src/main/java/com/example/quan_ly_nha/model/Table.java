package com.example.quan_ly_nha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@jakarta.persistence.Table(name = "restaurant_tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tableNumber;

    @Column
    private int capacity;

    @Column
    private boolean occupied = false;

    @Enumerated(EnumType.STRING)
    @Column
    private TableStatus status = TableStatus.AVAILABLE;

    public enum TableStatus {
        AVAILABLE,
        OCCUPIED,
        RESERVED,
        MAINTENANCE
    }
}