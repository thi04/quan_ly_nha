package com.example.quan_ly_nha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@jakarta.persistence.Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private Table table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String customerName;

    @Column
    private String customerPhone;

    @Column
    private String customerEmail;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    private Integer partySize;

    @Column
    private String specialRequest;

    @Enumerated(EnumType.STRING)
    @Column
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    public enum ReservationStatus {
        PENDING,
        CONFIRMED,
        COMPLETED,
        CANCELLED
    }
}