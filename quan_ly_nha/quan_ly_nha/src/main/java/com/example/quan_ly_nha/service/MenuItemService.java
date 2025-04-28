package com.example.quan_ly_nha.service;

import com.example.quan_ly_nha.dto.MenuItemDTO;
import com.example.quan_ly_nha.exception.ResourceNotFoundException;
import com.example.quan_ly_nha.model.Category;
import com.example.quan_ly_nha.model.MenuItem;
import com.example.quan_ly_nha.repository.CategoryRepository;
import com.example.quan_ly_nha.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::mapToMenuItemDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return mapToMenuItemDTO(menuItem);
    }

    public List<MenuItemDTO> getMenuItemsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        return menuItemRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToMenuItemDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getAvailableItems() {
        return menuItemRepository.findByAvailableTrue().stream()
                .map(this::mapToMenuItemDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        Category category = categoryRepository.findById(menuItemDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + menuItemDTO.getCategoryId()));

        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setImageUrl(menuItemDTO.getImageUrl());
        menuItem.setAvailable(menuItemDTO.isAvailable());
        menuItem.setCategory(category);

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return mapToMenuItemDTO(savedMenuItem);
    }

    @Transactional
    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO menuItemDTO) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        if (!menuItem.getCategory().getId().equals(menuItemDTO.getCategoryId())) {
            Category category = categoryRepository.findById(menuItemDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + menuItemDTO.getCategoryId()));
            menuItem.setCategory(category);
        }

        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setImageUrl(menuItemDTO.getImageUrl());
        menuItem.setAvailable(menuItemDTO.isAvailable());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return mapToMenuItemDTO(updatedMenuItem);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        menuItemRepository.delete(menuItem);
    }

    private MenuItemDTO mapToMenuItemDTO(MenuItem menuItem) {
        MenuItemDTO menuItemDTO = new MenuItemDTO();
        menuItemDTO.setId(menuItem.getId());
        menuItemDTO.setName(menuItem.getName());
        menuItemDTO.setDescription(menuItem.getDescription());
        menuItemDTO.setPrice(menuItem.getPrice());
        menuItemDTO.setImageUrl(menuItem.getImageUrl());
        menuItemDTO.setAvailable(menuItem.isAvailable());
        menuItemDTO.setCategoryId(menuItem.getCategory().getId());
        menuItemDTO.setCategoryName(menuItem.getCategory().getName());
        return menuItemDTO;
    }
}
