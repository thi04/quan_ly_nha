package com.example.quan_ly_nha.controller;

import com.example.quan_ly_nha.dto.MenuItemDTO;
import com.example.quan_ly_nha.service.MenuItemService;
import com.restaurant.dto.MenuItemDTO;
import com.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByCategoryId(categoryId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItemDTO>> getAvailableItems() {
        return ResponseEntity.ok(menuItemService.getAvailableItems());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) {
        return new ResponseEntity<>(menuItemService.createMenuItem(menuItemDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItemDTO menuItemDTO) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItemDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}