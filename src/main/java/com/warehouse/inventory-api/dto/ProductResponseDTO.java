package com.warehouse.inventory_api.dto;

import java.math.BigDecimal;

import com.warehouse.inventory_api.domain.Category;

public record ProductResponseDTO(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Category category) {
}
