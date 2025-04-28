package com.example.quan_ly_nha.controller;
import com.example.quan_ly_nha.dto.CategoryDTO;
import com.example.quan_ly_nha.dto.MenuItemDTO;
import com.example.quan_ly_nha.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(menuService.getAllCategories());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getCategoryById(id));
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(menuService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(menuService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        menuService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items")
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenuItemById(id));
    }

    @GetMapping("/categories/{categoryId}/items")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuService.getMenuItemsByCategory(categoryId));
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) {
        return new ResponseEntity<>(menuService.createMenuItem(menuItemDTO), HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemDTO menuItemDTO) {
        return ResponseEntity.ok(menuService.updateMenuItem(id, menuItemDTO));
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }