package com.warehouse.inventory_api.dto;

import java.math.BigDecimal;

import com.warehouse.inventory_api.domain.Category;

import jakarta.validation.constraints.*;

public record ProductRequestDTO(
        @NotBlank(message = "El SKU es obligatorio") String sku,

        @NotBlank(message = "El nombre es obligatorio") String name,

        String description,

        @NotNull(message = "El precio es obligatorio") @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0") BigDecimal price,

        @NotNull(message = "El stock es obligatorio") @PositiveOrZero(message = "El stock no puede ser negativo") Integer stock,

        @NotNull(message = "La categoría es obligatoria") Category category) {
}
