package com.example.quan_ly_nha.repository;


import com.example.quan_ly_nha.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByStatus(Table.TableStatus status);
    boolean existsByTableNumber(String tableNumber);

}