package com.example.quan_ly_nha.service;

import com.example.quan_ly_nha.dto.OrderDTO;
import com.example.quan_ly_nha.dto.OrderItemDTO;
import com.example.quan_ly_nha.exception.ResourceNotFoundException;
import com.example.quan_ly_nha.model.MenuItem;
import com.example.quan_ly_nha.model.Order;
import com.example.quan_ly_nha.model.OrderItem;
import com.example.quan_ly_nha.model.Table;
import com.example.quan_ly_nha.repository.MenuItemRepository;
import com.example.quan_ly_nha.repository.OrderRepository;
import com.example.quan_ly_nha.repository.TableRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, TableRepository tableRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    public List<OrderDTO> getOrdersByTable(Long tableId) {
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + tableId));
        return orderRepository.findByTable(table).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Table table = tableRepository.findById(orderDTO.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + orderDTO.getTableId()));

        Order order = new Order();
        order.setTable(table);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setItems(new ArrayList<>());

        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemDTO.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + itemDTO.getMenuItemId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setSubtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            orderItems.add(orderItem);
            total = total.add(orderItem.getSubtotal());
        }

        order.setItems(orderItems);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);

        if (status.equals("COMPLETED")) {
            order.setCompletionTime(LocalDateTime.now());
        }

        return convertToDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderDTO addItemToOrder(Long orderId, OrderItemDTO itemDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        MenuItem menuItem = menuItemRepository.findById(itemDTO.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + itemDTO.getMenuItemId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(itemDTO.getQuantity());
        orderItem.setUnitPrice(menuItem.getPrice());
        orderItem.setSubtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

        order.getItems().add(orderItem);
        order.setTotal(order.getTotal().add(orderItem.getSubtotal()));

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTableId(order.getTable().getId());
        dto.setOrderTime(order.getOrderTime());
        dto.setCompletionTime(order.getCompletionTime());
        dto.setStatus(order.getStatus());
        dto.setTotal(order.getTotal());

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setMenuItemId(item.getMenuItem().getId());
                    itemDTO.setMenuItemName(item.getMenuItem().getName());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setUnitPrice(item.getUnitPrice());
                    itemDTO.setSubtotal(item.getSubtotal());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}
